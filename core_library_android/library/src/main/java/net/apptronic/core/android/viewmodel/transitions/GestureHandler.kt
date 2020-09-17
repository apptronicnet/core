package net.apptronic.core.android.viewmodel.transitions

import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.Progress
import net.apptronic.core.mvvm.viewmodel.navigation.BackNavigationStatus

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
internal class GestureHandler(
    private val viewSwitch: ViewSwitch,
    private val gesture: TransitionGesture,
    private val gestureTarget: GestureTarget
) : TransitionGesture.Callback {

    private var isFinishing = false
    private var isCompleteCalled = false

    init {
        gesture.callback = this
    }

    private val transition: Transition<ViewSwitch> = gesture.createTransition(viewSwitch)

    private val player = TransitionPlayer(viewSwitch, transition).apply { start() }

    init {
        gestureTarget.getBackView()?.visibility = View.VISIBLE
    }

    fun onMotionEvent(event: MotionEvent): Boolean {
        if (isFinishing) {
            return true
        }
        return gesture.onMotionEvent(event)
    }

    fun onCancel() {
        isFinishing = true
        cancelWithDuration(gesture.cancelDuration, false)
    }

    override fun setProgress(progress: Progress) {
        player.seekTo(progress)
    }

    override fun cancelTransition(duration: Long) {
        if (isFinishing) {
            return
        }
        isFinishing = true
        cancelWithDuration(duration, false)
    }

    override fun completeTransition(duration: Long) {
        if (isFinishing) {
            return
        }
        if (gestureTarget.getBackNavigationStatus() == BackNavigationStatus.Allowed) {
            playAndFinish(duration, 1f, AccelerateInterpolator()) {
                gestureTarget.onGestureConfirmedPopBackStack()
            }
        } else {
            cancelWithDuration(gesture.cancelDuration, true)
        }
    }

    private fun cancelWithDuration(duration: Long, becauseOfRestricted: Boolean) {
        playAndFinish(duration, 0f, DecelerateInterpolator()) {
            gestureTarget.onGestureCancelled(becauseOfRestricted)
        }
    }

    private fun playAndFinish(
        duration: Long,
        toProgress: Progress,
        interpolator: Interpolator,
        onComplete: () -> Unit
    ) {
        player.play(
            gesture.progress, toProgress, duration, recallComplete = true
        ) {
            withInterpolator(interpolator)
            doOnCompleteOrCancel {
                if (!isCompleteCalled) {
                    isCompleteCalled = true
                    onComplete.invoke()
                }
            }
        }
    }

}
package net.apptronic.core.android.viewmodel.transitions

import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import net.apptronic.core.mvvm.viewmodel.navigation.BackNavigationStatus

internal class GestureHandler(
    private val containerView: View,
    private val frontView: View,
    private val backView: View,
    private val gesture: TransitionGesture,
    private val gestureTarget: GestureTarget
) : TransitionGesture.Callback {

    private var isFinishing = false
    private var isCompleteCalled = false

    init {
        gesture.callback = this
    }

    private val enterTransition: Transition<View> =
        gesture.createEnterTransition(containerView, backView) ?: EmptyViewTransition()

    private val exitTransition: Transition<View> =
        gesture.createExitTransition(containerView, frontView) ?: EmptyViewTransition()

    private val enterPlayer = TransitionPlayer(backView, enterTransition).apply { start() }
    private val exitPlayer = TransitionPlayer(frontView, exitTransition).apply { start() }

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

    override fun updateProgress(progress: Progress) {
        enterPlayer.seekTo(progress)
        exitPlayer.seekTo(progress)
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
        enterPlayer.play(
            gesture.progress, toProgress, duration, recallComplete = true
        ) {
            withInterpolator(interpolator)
        }
        exitPlayer.play(
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
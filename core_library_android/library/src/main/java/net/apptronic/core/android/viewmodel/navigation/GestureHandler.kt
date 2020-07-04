package net.apptronic.core.android.viewmodel.navigation

import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.EmptyViewTransition
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.Transition
import net.apptronic.core.android.viewmodel.transitions.TransitionPlayer

internal class GestureHandler(
    private val containerView: View,
    private val gesture: TransitionGesture,
    private val gestureTarget: GestureDispatcher.GestureTarget
) : TransitionGesture.Callback {

    private val backView = gestureTarget.getBackView()
    private val frontView = gestureTarget.getFrontView()

    private var isFinishing = false
    private var isCompleteCalled = false

    init {
        gesture.callback = this
    }

    private val enterTransition: Transition<View> = backView?.let {
        gesture.createEnterTransition(containerView, it)?.apply {
            onTransitionStarted(it)
        }
    } ?: EmptyViewTransition()
    private val exitTransition: Transition<View> = frontView?.let {
        gesture.createExitTransition(containerView, it)?.apply {
            onTransitionStarted(it)
        }
    } ?: EmptyViewTransition()

    private val enterPlayer = backView?.let { TransitionPlayer(it, enterTransition) }
    private val exitPlayer = frontView?.let { TransitionPlayer(it, exitTransition) }

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
        cancelWithDuration(gesture.cancelDuration)
    }

    override fun updateProgress(progress: Progress) {
        enterPlayer?.seekTo(progress)
        exitPlayer?.seekTo(progress)
    }

    override fun cancelTransition(duration: Long) {
        if (isFinishing) {
            return
        }
        isFinishing = true
        cancelWithDuration(duration)
    }

    override fun completeTransition(duration: Long) {
        if (isFinishing) {
            return
        }
        playAndFinish(duration, 1f, AccelerateInterpolator()) {
            gestureTarget.onGestureConfirmedPopBackStack()
        }
    }

    private fun cancelWithDuration(duration: Long) {
        playAndFinish(duration, 0f, DecelerateInterpolator()) {
            gestureTarget.onGestureCancelled()
        }
    }

    private fun playAndFinish(
        duration: Long,
        toProgress: Progress,
        interpolator: Interpolator,
        onComplete: () -> Unit
    ) {
        fun callComplete() {
            if (!isCompleteCalled) {
                isCompleteCalled = true
                onComplete.invoke()
            }
        }

        val transitions = mutableListOf<Transition<*>>()
        if (enterPlayer != null) {
            transitions += enterPlayer.play(
                gesture.progress, toProgress, duration, recallComplete = true
            ) {
                withInterpolator(interpolator)
                doOnCompleteOrCancel {
                    callComplete()
                }
            }
        }
        if (exitPlayer != null) {
            transitions += exitPlayer.play(
                gesture.progress, toProgress, duration, recallComplete = true
            ) {
                withInterpolator(interpolator)
                doOnCompleteOrCancel {
                    callComplete()
                }
            }
        }
        if (transitions.isEmpty()) {
            callComplete()
        }
    }

}
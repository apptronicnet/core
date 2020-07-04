package net.apptronic.core.android.viewmodel.navigation

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import net.apptronic.core.android.viewmodel.transitions.BackwardEnterTransition
import net.apptronic.core.android.viewmodel.transitions.BackwardExitTransition
import net.apptronic.core.android.viewmodel.transitions.PlaybackTransition
import net.apptronic.core.base.android.R
import kotlin.math.max
import kotlin.math.min

private const val ANIMATION_TIME = 300L
const val MAX_FLING_DETECTION_THRESHOLD = 0.25f

class BackStackNavigationFrameGestureAdapter : StackNavigationFrameGestureAdapter() {

    private class BackGesture(
        val containerView: View, val target: Target,
        val frontView: View, val backView: View,
        startEvent: MotionEvent
    ) : GestureDetector.SimpleOnGestureListener(), Gesture {

        private val enterTransition =
            BackwardEnterTransition(containerView).withInterpolator(LinearInterpolator())
        private val exitTransition =
            BackwardExitTransition(containerView).withInterpolator(LinearInterpolator())

        fun detectionThreshold(): Float = min(
            containerView.resources.getDimensionPixelSize(
                R.dimen.StackNavigationGesture_MaxDetectionThreshold
            ).toFloat(),
            containerView.width.toFloat() * MAX_FLING_DETECTION_THRESHOLD
        )

        private val startX: Float = startEvent.x
        private var isCancelled = false
        private var animateExit = false
        private var exitVelocity = 0f
        private var progress: Float = 0f

        private val gestureDetector = GestureDetector(containerView.context, this)

        init {
            enterTransition.onTransitionStarted(backView)
            exitTransition.onTransitionStarted(frontView)
            gestureDetector.onTouchEvent(startEvent)
        }

        private fun applyProgress() {
            enterTransition.applyTransition(backView, progress)
            exitTransition.applyTransition(frontView, progress)
        }

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent,
            velocityX: Float, velocityY: Float
        ): Boolean {
            val flingDistance = e2.x - e1.x
            if (flingDistance > detectionThreshold()) {
                if (velocityX > 0f) {
                    exitVelocity = velocityX
                    animateExit()
                } else {
                    onCancel()
                }
            }
            return true
        }

        override fun onMotionEvent(event: MotionEvent): Boolean {
            if (animateExit || isCancelled) {
                return true
            }
            gestureDetector.onTouchEvent(event)
            if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                progress = max(event.x - startX, 0f) / containerView.width
                applyProgress()
                return true
            }
            if (!isCancelled && event.action == MotionEvent.ACTION_UP) {
                val moveDistance = event.x - startX
                if (moveDistance > detectionThreshold()) {
                    animateExit()
                    return true
                }
            }
            return false
        }

        private fun animateExit() {
            val distanceToGo = containerView.width - frontView.translationX
            val exitDuration = if (exitVelocity > 0) {
                min(
                    ANIMATION_TIME, (distanceToGo / exitVelocity * 1000f).toLong()
                )
            } else ANIMATION_TIME
            animateExit(exitDuration)
        }

        private fun animateExit(exitDuration: Long) {
            if (animateExit) {
                return
            }
            animateExit = true

            PlaybackTransition(backView, progress, 1f)
                .withInterpolator(DecelerateInterpolator())
                .launch(enterTransition, exitDuration)
            PlaybackTransition(frontView, progress, 1f)
                .withInterpolator(DecelerateInterpolator())
                .doOnCompleteOrCancel {
                    target.onGestureConfirmedPopBackStack()
                }.launch(exitTransition, exitDuration)
        }

        override fun onCancel() {
            if (isCancelled) {
                return
            }
            isCancelled = true
            PlaybackTransition(backView, progress, 0f)
                .withInterpolator(DecelerateInterpolator())
                .launch(enterTransition, ANIMATION_TIME)
            PlaybackTransition(frontView, progress, 0f)
                .withInterpolator(DecelerateInterpolator())
                .doOnCompleteOrCancel {
                    target.onGestureCancelled()
                }.launch(exitTransition, ANIMATION_TIME)
        }

    }

    override fun onStartGesture(
        containerView: View, event: MotionEvent, target: Target
    ): Gesture? {
        val frontView = target.getFrontView()
        val backView = target.getBackView()
        if (frontView == null || backView == null) {
            return null
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            val threshold =
                containerView.resources.getDimensionPixelSize(R.dimen.StackNavigationGesture_EdgeThreshold)
            if (event.x < threshold) {
                backView.visibility = View.VISIBLE
                return BackGesture(
                    containerView,
                    target,
                    frontView,
                    backView,
                    event
                )
            }
        }
        return null
    }

}
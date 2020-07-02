package net.apptronic.core.android.viewmodel.navigation

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import net.apptronic.core.android.utils.TransitionAnimation
import net.apptronic.core.base.android.R
import kotlin.math.max
import kotlin.math.min

private const val ANIMATION_TIME = 300L

class BackStackNavigationFrameGestureAdapter : StackNavigationFrameGestureAdapter() {

    private class BackGesture(
        val touchableView: View, val target: Target,
        val frontView: View, val backView: View,
        startEvent: MotionEvent
    ) : GestureDetector.SimpleOnGestureListener(), Gesture {

        fun detectionThreshold(): Float = min(
            touchableView.resources.getDimensionPixelSize(
                R.dimen.StackNavigationGesture_MaxDetectionThreshold
            ).toFloat(),
            touchableView.width.toFloat() * 0.25f
        )

        private val backViewTranslation = 0.5f

        private fun backViewAnchorPoint(): Float {
            return -(touchableView.width * backViewTranslation)
        }

        private val startX: Float = startEvent.x
        private var isCancelled = false
        private var animateExit = false
        private var exitVelocity = 0f

        private val gestureDetector = GestureDetector(touchableView.context, this)

        init {
            gestureDetector.onTouchEvent(startEvent)
        }

        private fun setPosition(position: Float) {
            frontView.translationX = position
            backView.translationX = backViewAnchorPoint() + (position * backViewTranslation)
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
                val position = max(event.x - startX, 0f)
                setPosition(position)
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
            if (animateExit) {
                return
            }
            animateExit = true
            val distanceToGo = touchableView.width - frontView.translationX
            val exitDuration = if (exitVelocity > 0) {
                min(
                    ANIMATION_TIME, (distanceToGo / exitVelocity * 1000f).toLong()
                )
            } else ANIMATION_TIME

            TargetTransitionX(0f, DecelerateInterpolator(), View.VISIBLE).start(
                backView,
                exitDuration
            )
            TargetTransitionX(1f, DecelerateInterpolator(), View.GONE).doOnComplete {
                target.onGestureConfirmedPopBackStack()
            }.start(frontView, exitDuration)
        }

        override fun onCancel() {
            isCancelled = true
            TargetTransitionX(-backViewTranslation, DecelerateInterpolator(), View.GONE)
                .start(backView, ANIMATION_TIME)
            TargetTransitionX(0f, DecelerateInterpolator(), View.VISIBLE).doOnComplete {
                target.onGestureCancelled()
            }.start(frontView, ANIMATION_TIME)
        }

    }

    override fun onStartGesture(
        touchableView: View,
        event: MotionEvent,
        target: Target
    ): Gesture? {
        val frontView = target.getFrontView()
        val backView = target.getBackView()
        if (frontView == null || backView == null) {
            return null
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            val threshold =
                touchableView.resources.getDimensionPixelSize(R.dimen.StackNavigationGesture_EdgeThreshold)
            if (event.x < threshold) {
                backView.visibility = View.VISIBLE
                return BackGesture(
                    touchableView,
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

private class TargetTransitionX(
    val targetTransitionsXRelative: Float,
    override val interpolator: Interpolator,
    val endVisibility: Int
) : TransitionAnimation() {

    var startTranslationX: Float = 0f

    override fun onTransitionStarted(target: View) {
        super.onTransitionStarted(target)
        startTranslationX = target.translationX
    }

    override fun applyTransition(target: View, progress: Float) {
        super.applyTransition(target, progress)
        val targetTransitionX = target.width * targetTransitionsXRelative
        val distance = targetTransitionX - startTranslationX
        target.translationX = startTranslationX + (distance * progress)
    }

    override fun onTransitionCompleted(target: View) {
        super.onTransitionCompleted(target)
        target.visibility = endVisibility
    }

}
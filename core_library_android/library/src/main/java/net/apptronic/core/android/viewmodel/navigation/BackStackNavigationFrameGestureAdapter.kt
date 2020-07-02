package net.apptronic.core.android.viewmodel.navigation

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import net.apptronic.core.android.utils.onAnimationEnd
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
                exitVelocity = velocityX
                animateExit()
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
            if (event.action == MotionEvent.ACTION_UP) {
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

            val backViewStartX = backView.translationX
            backView.translationX = 0f
            backView.clearAnimation()
            backView.startAnimation(
                TranslateAnimation(backViewStartX, 0f, 0f, 0f).apply {
                    interpolator = DecelerateInterpolator()
                    duration = exitDuration
                    onAnimationEnd(backView) {
                        backView.visibility = View.VISIBLE
                        backView.translationX = 0f
                    }
                }
            )

            val frontViewStartX = frontView.translationX
            frontView.translationX = 0f
            frontView.clearAnimation()
            frontView.startAnimation(
                TranslateAnimation(frontViewStartX, touchableView.width.toFloat(), 0f, 0f).apply {
                    interpolator = DecelerateInterpolator()
                    duration = exitDuration
                    onAnimationEnd(frontView) {
                        frontView.translationX = 0f
                        backView.visibility = View.GONE
                        target.onGestureConfirmedPopBackStack()
                    }
                }
            )
        }

        override fun onCancel() {
            isCancelled = true
            val backViewStartX = backView.translationX
            backView.translationX = 0f
            backView.clearAnimation()
            backView.startAnimation(
                TranslateAnimation(backViewStartX, backViewAnchorPoint(), 0f, 0f).apply {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = ANIMATION_TIME
                    onAnimationEnd(backView) {
                        backView.visibility = View.GONE
                        backView.translationX = 0f
                    }
                }
            )

            val frontViewStartX = frontView.translationX
            frontView.translationX = 0f
            frontView.clearAnimation()
            frontView.startAnimation(
                TranslateAnimation(frontViewStartX, 0f, 0f, 0f).apply {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = ANIMATION_TIME
                    onAnimationEnd(frontView) {
                        frontView.translationX = 0f
                        target.onGestureCancelled()
                    }
                }
            )
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
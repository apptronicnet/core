package net.apptronic.core.android.viewmodel.navigation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import net.apptronic.core.android.viewmodel.transitions.*
import net.apptronic.core.base.android.R
import kotlin.math.max
import kotlin.math.min

private const val ANIMATION_TIME = 300L
const val OVERLAP = FORWARD_BACKWARD_OVERLAP
const val OVERLAY_ALPHA = MAX_OVERLAY_ALPHA
const val MAX_FLING_DETECTION_THRESHOLD = 0.25f

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
            touchableView.width.toFloat() * MAX_FLING_DETECTION_THRESHOLD
        )

        private fun backViewAnchorPoint(): Float {
            return -(touchableView.width * OVERLAP)
        }

        private val startX: Float = startEvent.x
        private var isCancelled = false
        private var animateExit = false
        private var exitVelocity = 0f

        private val gestureDetector = GestureDetector(touchableView.context, this)

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                backView.foreground = ColorDrawable(Color.BLACK)
                backView.foreground.setFloatAlpha(OVERLAY_ALPHA)
            }
            gestureDetector.onTouchEvent(startEvent)
        }

        private fun setPosition(position: Float) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                backView.foreground?.setFloatAlpha((1 - (position / touchableView.width)) * OVERLAY_ALPHA)
            }
            frontView.translationX = position
            backView.translationX = backViewAnchorPoint() + (position * OVERLAP)
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

            TargetTransitionX(0f, touchableView, 0f)
                .withInterpolator(DecelerateInterpolator())
                .doOnComplete {
                    backView.visibility = View.VISIBLE
                }.start(backView, exitDuration)
            TargetTransitionX(1f, touchableView)
                .withInterpolator(DecelerateInterpolator())
                .doOnComplete {
                    finishComplete()
                }.start(frontView, exitDuration)
        }

        private fun finishComplete() {
            frontView.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                backView.foreground = null
            }
            target.onGestureConfirmedPopBackStack()
        }

        override fun onCancel() {
            if (isCancelled) {
                return
            }
            isCancelled = true
            TargetTransitionX(-OVERLAP, touchableView, OVERLAY_ALPHA)
                .withInterpolator(DecelerateInterpolator())
                .doOnComplete { backView.visibility = View.GONE }
                .start(backView, ANIMATION_TIME)
            TargetTransitionX(0f, touchableView)
                .doOnComplete {
                    finishCancel()
                }.doOnCancel {
                    finishCancel()
                }.start(frontView, ANIMATION_TIME)
        }

        private fun finishCancel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                backView.foreground = null
            }
            target.onGestureCancelled()
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
    val container: View,
    val targetAlpha: Float? = null
) : Transition() {

    var startTranslationX: Float = 0f
    var startAlpha: Float = 0f

    override fun onTransitionStarted(target: View) {
        super.onTransitionStarted(target)
        startTranslationX = target.translationX
        if (targetAlpha != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startAlpha = (target.foreground?.alpha ?: 0).toFloat() / 255f
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        super.applyTransition(target, progress)
        val targetTransitionX = container.width * targetTransitionsXRelative
        val translationX = progress.interpolate(startTranslationX, targetTransitionX)
        target.translationX = translationX
        if (targetAlpha != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground?.setFloatAlpha(progress.interpolate(startAlpha, targetAlpha))
        }
    }

}
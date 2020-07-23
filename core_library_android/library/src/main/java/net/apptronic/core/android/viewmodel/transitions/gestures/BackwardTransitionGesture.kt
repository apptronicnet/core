package net.apptronic.core.android.viewmodel.transitions.gestures

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import net.apptronic.core.android.R
import net.apptronic.core.android.viewmodel.transitions.NavigationGestureDetector
import net.apptronic.core.android.viewmodel.transitions.Transition
import net.apptronic.core.android.viewmodel.transitions.TransitionGesture
import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.android.viewmodel.transitions.viewswitches.backwardTransition
import kotlin.math.max
import kotlin.math.min

private const val ANIMATION_TIME = 300L
private const val MAX_FLING_DETECTION_THRESHOLD = 0.25f

class BackwardTransitionGesture(
    private val containerView: View
) : TransitionGesture() {

    init {
        cancelDuration =
            ANIMATION_TIME
    }

    override fun createTransition(viewSwitch: ViewSwitch): Transition<ViewSwitch> {
        return viewSwitch.backwardTransition()
    }

    private fun detectionThreshold(): Float = min(
        containerView.resources.getDimensionPixelSize(
            R.dimen.StackNavigationGesture_MaxDetectionThreshold
        ).toFloat(),
        containerView.width.toFloat() * MAX_FLING_DETECTION_THRESHOLD
    )

    private var startX: Float = 0f
    private var exitVelocity = 0f

    override fun onStartEvent(event: MotionEvent) {
        gestureDetector.onTouchEvent(event)
        startX = event.x
    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {

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
                    cancelTransition()
                }
            }
            return true
        }

    }

    private val gestureDetector = GestureDetector(containerView.context, gestureListener)

    override fun onMotionEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        if (event.actionMasked == MotionEvent.ACTION_MOVE) {
            setProgress(max(event.x - startX, 0f) / containerView.width)
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
        val distanceToGo = containerView.width * (1f - getProgress())
        val exitDuration = if (exitVelocity > 0) {
            min(
                ANIMATION_TIME, (distanceToGo / exitVelocity * 1000f).toLong()
            )
        } else ANIMATION_TIME
        completeTransition(exitDuration)
    }

}

class BackwardTransitionGestureDetector :
    NavigationGestureDetector {

    override fun onStartGesture(
        event: MotionEvent, containerView: View, frontView: View, backView: View
    ): TransitionGesture? {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val threshold =
                containerView.resources.getDimensionPixelSize(R.dimen.StackNavigationGesture_EdgeThreshold)
            if (event.x < threshold) {
                return BackwardTransitionGesture(
                    containerView
                )
            }
        }
        return null
    }

}
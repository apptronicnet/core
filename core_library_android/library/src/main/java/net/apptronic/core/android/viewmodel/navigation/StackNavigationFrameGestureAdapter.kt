package net.apptronic.core.android.viewmodel.navigation

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

abstract class StackNavigationFrameGestureAdapter {

    interface Target {

        fun getFrontView(): View?

        fun getBackView(): View?

        fun onGestureConfirmedPopBackStack()

        fun onGestureCancelled()

    }

    interface Gesture {

        fun onMotionEvent(event: MotionEvent): Boolean

        fun onCancel()

    }

    private var touchableView: View? = null
    private var target: Target? = null
    private var gesture: Gesture? = null

    private val touchListener = object : View.OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return onTouchEventDetected(v, event)
        }
    }

    fun attach(touchableView: View, target: Target) {
        this.touchableView = touchableView
        this.target = target
        touchableView.setOnTouchListener(touchListener)
    }

    fun reset() {
        gesture?.onCancel()
        gesture = null
    }

    @SuppressLint("ClickableViewAccessibility")
    fun detach() {
        reset()
        touchableView?.setOnTouchListener(null)
        touchableView = null
        target = null
    }

    private inner class TargetWrapper(
        private val target: Target
    ) : Target by target {

        override fun onGestureConfirmedPopBackStack() {
            this@StackNavigationFrameGestureAdapter.gesture = null
            target.onGestureConfirmedPopBackStack()
        }

        override fun onGestureCancelled() {
            this@StackNavigationFrameGestureAdapter.gesture = null
            target.onGestureCancelled()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchEventDetected(v: View, event: MotionEvent): Boolean {
        val touchableView = this.touchableView
        val target = this.target
        if (touchableView != null && target != null) {
            val gesture = this.gesture
            if (gesture == null) {
                this.gesture = onStartGesture(touchableView, event, TargetWrapper(target))
                return this.gesture != null
            } else {
                val handled = gesture.onMotionEvent(event)
                if (!handled) {
                    gesture.onCancel()
                }
                return handled
            }
        }
        return false
    }

    abstract fun onStartGesture(containerView: View, event: MotionEvent, target: Target): Gesture?

}
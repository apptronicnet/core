package net.apptronic.core.android.viewmodel.navigation

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

internal class GestureDispatcher(
    private val gestureDetector: NavigationGestureDetector
) {

    interface GestureTarget {

        fun getFrontView(): View?

        fun getBackView(): View?

        fun onGestureStarted()

        fun onGestureConfirmedPopBackStack()

        fun onGestureCancelled()

    }

    private var touchableView: View? = null
    private var gestureTarget: GestureTarget? = null
    private var gestureHandler: GestureHandler? = null

    private val touchListener = object : View.OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return onTouchEventDetected(v, event)
        }
    }

    fun attach(touchableView: View, gestureTarget: GestureTarget) {
        this.touchableView = touchableView
        this.gestureTarget = gestureTarget
        touchableView.setOnTouchListener(touchListener)
    }

    fun reset() {
        gestureHandler?.onCancel()
        gestureHandler = null
    }

    @SuppressLint("ClickableViewAccessibility")
    fun detach() {
        reset()
        touchableView?.setOnTouchListener(null)
        touchableView = null
        gestureTarget = null
    }

    private inner class GestureTargetWrapper(
        private val gestureTarget: GestureTarget
    ) : GestureTarget by gestureTarget {

        override fun onGestureConfirmedPopBackStack() {
            this@GestureDispatcher.gestureHandler = null
            gestureTarget.onGestureConfirmedPopBackStack()
        }

        override fun onGestureCancelled() {
            this@GestureDispatcher.gestureHandler = null
            gestureTarget.onGestureCancelled()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchEventDetected(v: View, event: MotionEvent): Boolean {
        val touchableView = this.touchableView
        val target = this.gestureTarget
        if (touchableView != null && target != null) {
            val gesture = this.gestureHandler
            if (gesture == null) {
                val transitionGesture =
                    gestureDetector.onStartGesture(
                        event, touchableView, target.getFrontView(), target.getBackView()
                    )
                if (transitionGesture != null) {
                    target.onGestureStarted()
                    transitionGesture.onStartEvent(event)
                    gestureHandler = GestureHandler(
                        touchableView, transitionGesture, GestureTargetWrapper(target)
                    )
                }
                return this.gestureHandler != null
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

}
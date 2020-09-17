package net.apptronic.core.android.viewmodel.transitions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import net.apptronic.core.mvvm.viewmodel.navigation.BackNavigationStatus

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
internal class GestureDispatcher(
    private val gestureDetector: NavigationGestureDetector
) {

    private var touchableView: View? = null
    private var gestureTarget: GestureTarget? = null
    private var activeGesture: GestureHandler? = null

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
        activeGesture?.onCancel()
        activeGesture = null
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
            this@GestureDispatcher.activeGesture = null
            gestureTarget.onGestureConfirmedPopBackStack()
        }

        override fun onGestureCancelled(becauseOfRestricted: Boolean) {
            this@GestureDispatcher.activeGesture = null
            gestureTarget.onGestureCancelled(becauseOfRestricted)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchEventDetected(v: View, event: MotionEvent): Boolean {
        val touchableView = this.touchableView
        val target = this.gestureTarget
        if (touchableView != null && target != null) {
            if (target.getBackNavigationStatus() == BackNavigationStatus.Disabled) {
                activeGesture?.onCancel()
                return false
            }
            val activeGesture = this.activeGesture
            if (activeGesture == null) {
                val frontView = target.getFrontView()
                val backView = target.getBackView()
                if (frontView == null || backView == null) {
                    return false
                }
                val transitionGesture =
                    gestureDetector.onStartGesture(
                        event, touchableView, frontView, backView
                    )
                if (transitionGesture != null) {
                    target.onGestureStarted()
                    transitionGesture.onStartEvent(event)
                    val viewSwitch = ViewSwitch(
                        entering = backView,
                        exiting = frontView,
                        isNewOnFront = false,
                        container = touchableView
                    )
                    this.activeGesture =
                        GestureHandler(viewSwitch, transitionGesture, GestureTargetWrapper(target))
                }
                return this.activeGesture != null
            } else {
                val handled = activeGesture.onMotionEvent(event)
                if (!handled) {
                    activeGesture.onCancel()
                }
                return handled
            }
        }
        return false
    }

}
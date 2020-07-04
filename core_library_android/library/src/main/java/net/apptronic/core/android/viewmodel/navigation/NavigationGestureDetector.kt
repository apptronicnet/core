package net.apptronic.core.android.viewmodel.navigation

import android.view.MotionEvent
import android.view.View

interface NavigationGestureDetector {

    fun onStartGesture(
        event: MotionEvent,
        containerView: View,
        frontView: View?,
        backView: View?
    ): TransitionGesture?

}
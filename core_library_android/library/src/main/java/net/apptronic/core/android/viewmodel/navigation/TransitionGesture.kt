package net.apptronic.core.android.viewmodel.navigation

import android.view.MotionEvent
import android.view.View
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.Transition

abstract class TransitionGesture {

    internal interface Callback {

        fun updateProgress(progress: Progress)

        fun cancelTransition(duration: Long)

        fun completeTransition(duration: Long)

    }

    internal var callback: Callback? = null

    var cancelDuration: Long = 0L
    internal var progress: Progress = 0f

    fun getProgress(): Progress {
        return progress
    }

    abstract fun createEnterTransition(containerView: View, backView: View): Transition<View>?

    abstract fun createExitTransition(containerView: View, frontView: View): Transition<View>?

    abstract fun onStartEvent(event: MotionEvent)

    abstract fun onMotionEvent(event: MotionEvent): Boolean

    fun transitionProgress(progress: Progress) {
        this.progress = progress
        if (this.progress < 0f) {
            this.progress = 0f
        }
        if (this.progress > 1f) {
            this.progress = 1f
        }
        callback?.updateProgress(this.progress)
    }

    fun completeTransition(duration: Long) {
        callback?.completeTransition(duration)
    }

    fun cancelTransition(duration: Long = cancelDuration) {
        callback?.cancelTransition(cancelDuration)
    }

}
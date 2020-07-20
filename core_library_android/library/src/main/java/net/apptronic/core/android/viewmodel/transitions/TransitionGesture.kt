package net.apptronic.core.android.viewmodel.transitions

import android.view.MotionEvent

abstract class TransitionGesture {

    internal interface Callback {

        fun setProgress(progress: Progress)

        fun cancelTransition(duration: Long)

        fun completeTransition(duration: Long)

    }

    internal var callback: Callback? = null

    var cancelDuration: Long = 0L
    internal var progress: Progress = 0f

    fun getProgress(): Progress {
        return progress
    }

    abstract fun createTransition(viewSwitch: ViewSwitch): Transition<ViewSwitch>

    abstract fun onStartEvent(event: MotionEvent)

    abstract fun onMotionEvent(event: MotionEvent): Boolean

    fun setProgress(progress: Progress) {
        this.progress = progress
        if (this.progress < 0f) {
            this.progress = 0f
        }
        if (this.progress > 1f) {
            this.progress = 1f
        }
        callback?.setProgress(this.progress)
    }

    fun completeTransition(duration: Long) {
        callback?.completeTransition(duration)
    }

    fun cancelTransition(duration: Long = cancelDuration) {
        callback?.cancelTransition(cancelDuration)
    }

}
package net.apptronic.core.android.viewmodel.transitions

import android.os.SystemClock
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private object FrameScheduler {

    interface FrameListener {

        /**
         * Called when next frame can be drawn
         * @return is listener need to listen next frame
         */
        fun onNextFrame(timestamp: Long): Boolean

    }

    private val dispatcher = Dispatchers.Main

    private val frameListeners = mutableListOf<FrameListener>()

    fun addListener(frameListener: FrameListener) {
        val needStart = frameListeners.isEmpty()
        frameListeners.add(frameListener)
        if (needStart) {
            scheduleFrame()
        }
    }

    private fun scheduleFrame() {
        GlobalScope.launch(dispatcher) {
            sendFrame()
            if (frameListeners.isNotEmpty()) {
                scheduleFrame()
            }
        }
    }

    private fun sendFrame() {
        val timestamp = SystemClock.elapsedRealtime()
        frameListeners.removeAll {
            it.onNextFrame(timestamp).not()
        }
    }

}

typealias Progress = Float

abstract class Transition<Target> : FrameScheduler.FrameListener {

    var interpolator: Interpolator = LinearInterpolator()
    var duration: Long = 0

    open val isFrontTransition = false

    private var isStarting = false
    private var isIntercepting = false
    private var progressInterpolator: Interpolator = interpolator
    private var target: Target? = null
    private var start: Long = 0
    private var end: Long = 0
    private var doOnStartActions = mutableListOf<() -> Unit>()
    private var doOnCompleteActions = mutableListOf<() -> Unit>()
    private var doOnCancelActions = mutableListOf<() -> Unit>()

    fun withInterpolator(interpolator: Interpolator): Transition<Target> {
        this.interpolator = interpolator
        return this
    }

    fun withDuration(duration: Long): Transition<Target> {
        this.duration = duration
        return this
    }

    fun doOnStart(action: () -> Unit): Transition<Target> {
        doOnStartActions.add(action)
        return this
    }

    fun doOnComplete(action: () -> Unit): Transition<Target> {
        doOnCompleteActions.add(action)
        return this
    }

    fun doOnCancel(action: () -> Unit): Transition<Target> {
        doOnCancelActions.add(action)
        return this
    }

    fun doOnCompleteOrCancel(action: () -> Unit): Transition<Target> {
        doOnCompleteActions.add(action)
        doOnCancelActions.add(action)
        return this
    }

    fun launch(target: Target) {
        cancel()
        this.target = target
        progressInterpolator = interpolator
        val runningTransition = getRunningTransition(target)
        setRunningTransition(target)
        if (runningTransition != null) {
            isIntercepting = true
            onTransitionIntercepted(target)
            runningTransition.cancel()
        } else {
            isIntercepting = false
        }
        if (duration <= 0) {
            doStartInternal(target, SystemClock.elapsedRealtime())
            completeTransition(target)
        } else {
            isStarting = true
            FrameScheduler.addListener(this)
        }
    }

    abstract fun getRunningTransition(target: Target): Transition<Target>?

    abstract fun setRunningTransition(target: Target)

    abstract fun clearRunningTransition(target: Target)

    abstract fun isAllowsTransition(target: Target): Boolean

    fun launch(view: Target, duration: Long) {
        this.duration = duration
        launch(view)
    }

    private fun doStartInternal(target: Target, startTime: Long) {
        start = startTime
        end = start + duration
        if (!isIntercepting) {
            onTransitionStarted(target)
        }
        isIntercepting = false
        doOnStartActions.forEach { it.invoke() }
    }

    fun cancel() {
        isStarting = false
        val target = this.target
        if (target != null) {
            onTransitionCancelled(target)
            doOnCancelActions.forEach { it.invoke() }
            clearRunningTransition(target)
            this.target = null
        }
    }

    private fun completeTransition(target: Target) {
        applyTransition(target, 1f)
        onTransitionCompleted(target)
        doOnCompleteActions.forEach { it.invoke() }
        clearRunningTransition(target)
    }

    override fun onNextFrame(timestamp: Long): Boolean {
        val target = this.target ?: return false
        if (isStarting) {
            doStartInternal(target, timestamp)
            isStarting = false
            onTransitionStarted(target)
        }
        if (!isAllowsTransition(target)) {
            onTransitionCancelled(target)
            return false
        }
        if (timestamp <= end) {
            val duration = end - start
            val progress = (timestamp - start).toFloat() / duration.toFloat()
            applyFrame(target, progress)
            return true
        } else {
            completeTransition(target)
        }
        return false
    }

    private fun applyFrame(target: Target, progress: Float) {
        val interpolated = progressInterpolator.getInterpolation(progress)
        applyTransition(target, interpolated)
    }

    /**
     * Called when on transition start another transition is running on this view
     * By default recalls [onTransitionStarted]
     */
    open fun onTransitionIntercepted(target: Target) {
        onTransitionStarted(target)
    }

    open fun onTransitionStarted(target: Target) {
        // implement by subclasses if needed
    }

    abstract fun applyTransition(target: Target, progress: Progress)

    open fun onTransitionCompleted(target: Target) {
        // implement by subclasses if needed
    }

    open fun onTransitionCancelled(target: Target) {
        // implement by subclasses if needed
    }

}
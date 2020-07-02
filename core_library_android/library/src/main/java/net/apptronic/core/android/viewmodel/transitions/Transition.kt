package net.apptronic.core.android.viewmodel.transitions

import android.os.SystemClock
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import net.apptronic.core.base.android.R

typealias Progress = Float

open class Transition {

    var interpolator: Interpolator = LinearInterpolator()
    var duration: Long = 0

    open val isFrontTransition = false

    private var isRunning = false
    private var progressInterpolator: Interpolator = interpolator
    private var target: View? = null
    private var start: Long = 0
    private var end: Long = 0
    private var doOnStartActions = mutableListOf<() -> Unit>()
    private var doOnCompleteActions = mutableListOf<() -> Unit>()
    private var doOnCancelActions = mutableListOf<() -> Unit>()

    fun withInterpolator(interpolator: Interpolator): Transition {
        this.interpolator = interpolator
        return this
    }

    fun withDuration(duration: Long): Transition {
        this.duration = duration
        return this
    }

    fun doOnStart(action: () -> Unit): Transition {
        doOnStartActions.add(action)
        return this
    }

    fun doOnComplete(action: () -> Unit): Transition {
        doOnCompleteActions.add(action)
        return this
    }

    fun doOnCancel(action: () -> Unit): Transition {
        doOnCancelActions.add(action)
        return this
    }

    fun start(view: View) {
        cancel()
        progressInterpolator = interpolator
        target = view
        val runningTransition = (view.getTag(R.id.TransitionAnimation) as? Transition)
        view.setTag(R.id.TransitionAnimation, this)
        start = SystemClock.elapsedRealtime()
        end = start + duration

        if (runningTransition != null) {
            onTransitionIntercepted(view)
            runningTransition.cancel()
        } else {
            onTransitionStarted(view)
        }

        doOnStartActions.forEach { it.invoke() }
        nextFrame()
    }

    fun start(view: View, duration: Long) {
        this.duration = duration
        start(view)
    }

    fun cancel() {
        val target = this.target
        if (target != null) {
            onTransitionCancelled(target)
            doOnCancelActions.forEach { it.invoke() }
            target.setTag(R.id.TransitionAnimation, null)
            isRunning = false
            this.target = null
        }
    }

    private fun nextFrame() {
        val target = this.target
        if (target != null) {
            val time = SystemClock.elapsedRealtime()
            if (time <= end) {
                val duration = end - start
                val progress = (time - start).toFloat() / duration.toFloat()
                applyFrame(target, progress)
                target.post {
                    nextFrame()
                }
            } else {
                onTransitionCompleted(target)
                doOnCompleteActions.forEach { it.invoke() }
                target.setTag(R.id.TransitionAnimation, null)
            }
        }
    }

    internal open fun applyFrame(target: View, progress: Float) {
        val interpolated = progressInterpolator.getInterpolation(progress)
        applyTransition(target, interpolated)
    }

    open fun onTransitionIntercepted(target: View) {
        onTransitionStarted(target)
    }

    open fun onTransitionStarted(target: View) {

    }

    open fun applyTransition(target: View, progress: Progress) {

    }

    open fun onTransitionCompleted(target: View) {
        applyTransition(target, 1f)
    }

    open fun onTransitionCancelled(target: View) {

    }

}
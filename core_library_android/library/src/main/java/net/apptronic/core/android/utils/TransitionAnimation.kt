package net.apptronic.core.android.utils

import android.os.SystemClock
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import net.apptronic.core.base.android.R

open class TransitionAnimation {

    open val interpolator: Interpolator = LinearInterpolator()

    private var isRunning = false
    private var target: View? = null
    private var start: Long = 0
    private var end: Long = 0
    private var doOnStartActions = mutableListOf<() -> Unit>()
    private var doOnCompleteActions = mutableListOf<() -> Unit>()
    private var doOnCancelActions = mutableListOf<() -> Unit>()

    fun doOnStart(action: () -> Unit): TransitionAnimation {
        doOnStartActions.add(action)
        return this
    }

    fun doOnComplete(action: () -> Unit): TransitionAnimation {
        doOnCompleteActions.add(action)
        return this
    }

    fun doOnCancel(action: () -> Unit): TransitionAnimation {
        doOnCancelActions.add(action)
        return this
    }

    fun start(view: View, duration: Long) {
        cancel()
        (view.getTag(R.id.TransitionAnimation) as? TransitionAnimation)?.cancel()
        target = view
        view.setTag(R.id.TransitionAnimation, this)
        start = SystemClock.elapsedRealtime()
        end = start + duration
        onTransitionStarted(view)
        doOnStartActions.forEach { it.invoke() }
        nextFrame()
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
                val interpolated = interpolator.getInterpolation(progress)
                applyTransition(target, interpolated)
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

    open fun onTransitionStarted(target: View) {

    }

    open fun applyTransition(target: View, progress: Float) {

    }

    open fun onTransitionCompleted(target: View) {
        applyTransition(target, 1f)
    }

    open fun onTransitionCancelled(target: View) {

    }

}
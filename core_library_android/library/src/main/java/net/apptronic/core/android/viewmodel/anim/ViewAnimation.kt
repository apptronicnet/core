package net.apptronic.core.android.viewmodel.anim

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.R
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolateWith

class ViewAnimation internal constructor(
    private val target: View,
    private val container: View,
    private val transformationSet: ViewTransformationSet,
    private val duration: Long,
    private val interpolator: Interpolator
) {

    private val onStartActions = mutableListOf<() -> Unit>()
    private val onCompleteActions = mutableListOf<() -> Unit>()
    private val onCancelActions = mutableListOf<() -> Unit>()

    fun doOnStart(action: () -> Unit): ViewAnimation {
        onStartActions.add(action)
        return this
    }

    fun doOnComplete(action: () -> Unit): ViewAnimation {
        onCompleteActions.add(action)
        return this
    }

    fun doOnCancel(action: () -> Unit): ViewAnimation {
        onCancelActions.add(action)
        return this
    }

    fun doOnCompleteOrCancel(action: () -> Unit): ViewAnimation {
        onCompleteActions.add(action)
        onCancelActions.add(action)
        return this
    }

    private var isStarted = false
    private var isCompleted = false
    private var isCancelled = false
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var intercepting: ViewAnimation? = null

    internal fun start() {
        intercepting = target.getTag(R.id.ViewAnimation) as? ViewAnimation
        intercepting?.cancel()
        target.setTag(R.id.ViewAnimation, this)
        onStartActions.forEach { it() }
        if (duration == 0L) {
            complete()
        }
    }

    /**
     * Play animation frame
     * Return true if animation should be removed from frame queue
     */
    internal fun playFrame(timestamp: Long): Boolean {
        if (isCancelled || isCompleted) {
            return true
        }
        if (!isStarted) {
            isStarted = true
            startTime = timestamp
            endTime = startTime + duration
            transformationSet.start(target, container, intercepting?.transformationSet)
            intercepting = null
        }
        return if (timestamp >= endTime) {
            complete()
            isCompleted = true
            true
        } else {
            val progress: Progress = (timestamp - startTime).toFloat() / duration.toFloat()
            transformationSet.transform(target, container, progress.interpolateWith(interpolator))
            false
        }
    }

    private fun complete() {
        onCompleteActions.forEach { it() }
        target.setTag(R.id.ViewAnimation, null)
        transformationSet.reset(target, container)
    }

    private fun cancel() {
        onCancelActions.forEach { it() }
        isCancelled = true
    }

    fun applyProgress(progress: Progress) {
        if (!isStarted) {
            start()
            isStarted = true
            transformationSet.start(target, container, null)
        }
        transformationSet.transform(target, container, progress)
    }

    fun playOn(player: AnimationPlayer) {
        player.playAnimation(this)
    }

}
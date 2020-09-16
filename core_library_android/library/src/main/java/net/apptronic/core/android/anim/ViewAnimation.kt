package net.apptronic.core.android.anim

import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.R
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolateWith

class ViewAnimation internal constructor(
    val target: View,
    private val container: View,
    private val transformationSet: ViewTransformationSet,
    private val duration: Long,
    private val interpolator: Interpolator
) {

    private val onStartActions = mutableListOf<() -> Unit>()
    private val onCompleteActions = mutableListOf<() -> Unit>()
    private val onCancelActions = mutableListOf<() -> Unit>()
    private var player: AnimationPlayer? = null

    private class Next(
        val player: AnimationPlayer,
        val animation: ViewAnimation,
        val intercept: Boolean
    ) {
        fun start() {
            player.playAnimation(animation, intercept)
        }
    }

    private var next: Next? = null

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
    private var isFinalized = false
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var intercepting: ViewAnimation? = null

    override fun toString(): String {
        return "ViewAnimation[${hashCode()}]"
    }

    internal fun start(player: AnimationPlayer, intercept: Boolean) {
        Log.d("ViewAnimation", "Launching $this")
        val intercepting = target.getTag(R.id.ViewAnimation) as? ViewAnimation
        if (intercepting != null && !intercept) {
            Log.d("ViewAnimation", "$this adding to queue after $intercepting")
            intercepting.next = Next(player, this, intercept)
            return
        }
        this.player = player
        doStartInternal(intercepting)
    }

    private fun doStartInternal(intercepting: ViewAnimation?) {
        this.intercepting = intercepting
        if (intercepting != null) {
            Log.d("ViewAnimation", "$this intercepted $intercepting")
        }
        intercepting?.cancel(duration == 0L)
        target.setTag(R.id.ViewAnimation, this)
        onStartActions.forEach { it() }
        if (duration == 0L) {
            Log.d("ViewAnimation", "$this completed immediately")
            isCompleted = true
            intercepting?.transformationSet?.reset(target, container)
            transformationSet.transform(target, container, 1f)
            finalize()
        } else {
            Log.d("ViewAnimation", "$this running")
            player?.onAnimationStarted(this)
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
            Log.d("ViewAnimation", "$this completed")
            isCompleted = true
            transformationSet.transform(target, container, 1f)
            target.post {
                finalize()
            }
            true
        } else {
            val progress: Progress = (timestamp - startTime).toFloat() / duration.toFloat()
            transformationSet.transform(target, container, progress.interpolateWith(interpolator))
            false
        }
    }

    private fun finalize() {
        if (!isCancelled && isFinalized) {
            return
        }
        Log.d("ViewAnimation", "$this finalized")
        isFinalized = true
        target.setTag(R.id.ViewAnimation, null)
        transformationSet.reset(target, container)
        onCompleteActions.forEach { it() }
        next?.start()
    }

    private fun cancel(reset: Boolean) {
        if (isFinalized && isCancelled) {
            return
        }
        if (reset) {
            transformationSet.reset(target, container)
        }
        Log.d("ViewAnimation", "$this cancelled")
        onCancelActions.forEach { it() }
        target.setTag(R.id.ViewAnimation, null)
        isCancelled = true
    }

    fun playOn(player: AnimationPlayer, intercept: Boolean = true) {
        player.playAnimation(this, intercept)
    }

}
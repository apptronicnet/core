package net.apptronic.core.ios.anim

import platform.UIKit.UIView

class ViewAnimation(
        val target: UIView,
        val container: UIView,
        val definition: ViewAnimationDefinition,
        val duration: Long
) {

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

    private val onStartActions = mutableListOf<() -> Unit>()
    private val onCompleteActions = mutableListOf<() -> Unit>()
    private val onCancelActions = mutableListOf<() -> Unit>()

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

    private fun callComplete() {
        if (!isCancelled && !isCompeted) {
            isCompeted = true
            onCompleteActions.forEach { it() }
            next?.start()
        }
    }

    internal fun start(player: AnimationPlayer, intercept: Boolean, current: ViewAnimation?) {
        val intercepting = current
        if (intercepting != null && !intercept) {
            intercepting.next = Next(player, this, intercept)
            return
        }
        this.player = player
        doPlay()
    }

    private var transformation: ViewTransformation? = null
    private var isCompeted: Boolean = false
    private var isCancelled: Boolean = false

    private fun doPlay() {
        onStartActions.forEach { it() }
        transformation = definition.createTransformation()
        transformation?.onComplete = this::callComplete
        transformation?.animate(target, container, duration.toDouble())
    }

    fun cancel() {
        if (!isCancelled && !isCompeted) {
            isCancelled = true
            onCancelActions.forEach { it() }
        }
    }

    fun playOn(player: AnimationPlayer, intercept: Boolean) {
        player.playAnimation(this, intercept)
    }

}
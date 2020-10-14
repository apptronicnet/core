package net.apptronic.core.ios.anim

import platform.UIKit.UIView

abstract class ViewTransformation {

    internal var isCancelled: Boolean = false

    val UIView.isNotAnimating: Boolean
        get() {
            return layer.animationKeys()?.isNotEmpty() != true
        }

    fun UIView.prepareAnimation(prepare: UIView.() -> Unit) {
        if (isNotAnimating) {
            prepare()
        }
    }

    internal var onComplete: () -> Unit = {}

    protected fun completed(param: Boolean) {
        if (!isCancelled) {
            onComplete()
        }
    }

    abstract fun animate(target: UIView, container: UIView, duration: Double)

    abstract fun cancel(target: UIView, container: UIView)

}
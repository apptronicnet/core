package net.apptronic.core.ios.anim

import platform.UIKit.UIView

abstract class ViewAnimationDefinition {

    abstract fun createTransformation(): ViewTransformation

    fun createAnimation(
            target: UIView, container: UIView, duration: Long
    ): ViewAnimation {
        return ViewAnimation(target, container, this, duration)
    }

}
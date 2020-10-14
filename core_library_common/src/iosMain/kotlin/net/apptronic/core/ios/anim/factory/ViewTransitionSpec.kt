package net.apptronic.core.ios.anim.factory

import net.apptronic.core.ios.anim.transition.ViewTransitionDirection
import platform.UIKit.UIView

class ViewTransitionSpec(
        val enter: UIView,
        val exit: UIView,
        val container: UIView,
        val duration: Long,
        val transitionSpec: Any?,
        val direction: ViewTransitionDirection? = null
)
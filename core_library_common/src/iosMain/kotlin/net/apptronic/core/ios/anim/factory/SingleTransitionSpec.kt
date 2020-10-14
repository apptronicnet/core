package net.apptronic.core.ios.anim.factory

import platform.UIKit.UIView

class SingleTransitionSpec(
        val target: UIView,
        val container: UIView,
        val duration: Long,
        val transitionSpec: Any?
)
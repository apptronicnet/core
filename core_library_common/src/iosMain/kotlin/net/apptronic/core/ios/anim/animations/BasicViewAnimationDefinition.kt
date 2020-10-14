package net.apptronic.core.ios.anim.animations

import net.apptronic.core.ios.anim.ViewAnimationDefinition
import net.apptronic.core.ios.anim.ViewTransformation
import platform.UIKit.UIViewAnimationCurve

abstract class BasicViewAnimationDefinition(
        private val animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : ViewAnimationDefinition() {

    final override fun createTransformation(): ViewTransformation {
        return createBasicViewTransformation().also {
            it.animationCurve = animationCurve
        }
    }

    abstract fun createBasicViewTransformation(): BasicViewTransformation

}
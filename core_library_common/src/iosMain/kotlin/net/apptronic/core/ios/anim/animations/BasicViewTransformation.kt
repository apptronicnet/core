package net.apptronic.core.ios.anim.animations

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readValue
import net.apptronic.core.ios.anim.ViewTransformation
import platform.CoreGraphics.CGRect
import platform.UIKit.UIView
import platform.UIKit.UIViewAnimationCurve
import platform.UIKit.animateWithDuration
import platform.UIKit.setFrame

abstract class BasicViewTransformation : ViewTransformation() {

    internal var animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear

    fun translateToParent(target: UIView, container: UIView, x: Float = 0f, y: Float = 0f) {
        memScoped {
            val frame = container.frame.getPointer(this).pointed
            val translationX = frame.size.width * x
            val translationY = frame.size.height * y
            val targetFrame = alloc<CGRect>()
            targetFrame.origin.x = frame.origin.x + translationX
            targetFrame.origin.y = frame.origin.y + translationY
            targetFrame.size.width = frame.size.width
            targetFrame.size.height = frame.size.height
            target.setFrame(targetFrame.readValue())
        }
    }

    fun startAnimation(duration: Number, animations: () -> Unit) {
        UIView.animateWithDuration(
                duration = duration.toDouble(),
                delay = 0.0,
                options = animationCurve.ordinal.toULong(),
                animations = animations,
                completion = this::completed
        )
    }

}
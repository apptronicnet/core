package net.apptronic.core.view.shape

import net.apptronic.core.component.value
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext

class CoreRectangleShapeDrawable internal constructor(context: CoreViewContext) : CoreShapeView(context) {

    var cornerTopLeft = value<Number>(0)
    var cornerTopRight = value<Number>(0)
    var cornerBottomLeft = value<Number>(0)
    var cornerBottomRight = value<Number>(0)

    fun corners(
            all: Number? = null,
            left: Number? = null,
            right: Number? = null,
            top: Number? = null,
            bottom: Number? = null,
            topLeft: Number? = null,
            topRight: Number? = null,
            bottomLeft: Number? = null,
            bottomRight: Number? = null
    ) {
        all?.let { cornerTopLeft.set(it); cornerTopRight.set(it); cornerBottomLeft.set(it); cornerBottomRight.set(it) }
        left?.let { cornerTopLeft.set(it); cornerBottomLeft.set(it) }
        right?.let { cornerTopRight.set(it); cornerBottomRight.set(it) }
        top?.let { cornerTopLeft.set(it); cornerTopRight.set(it) }
        bottom?.let { cornerBottomLeft.set(it); cornerBottomRight.set(it) }
        topLeft?.let { cornerTopLeft.set(it) }
        topRight?.let { cornerTopRight.set(it) }
        bottomLeft?.let { cornerBottomLeft.set(it) }
        bottomRight?.let { cornerBottomRight.set(it) }
    }

}

fun CoreViewBuilder.rectangleShape(builder: CoreRectangleShapeDrawable.() -> Unit): ICoreView {
    return nextView(::CoreRectangleShapeDrawable, builder)
}
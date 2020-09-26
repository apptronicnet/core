package net.apptronic.core.view.shape

import net.apptronic.core.view.properties.CoreColor

class CoreRectangleShapeDrawable internal constructor() : CoreDrawable {
    var fillColor: CoreColor? = null
    var strokeColor: CoreColor? = null
    var strokeWidth: Number? = null
    var cornerTopLeft: Number = 0
    var cornerTopRight: Number = 0
    var cornerBottomLeft: Number = 0
    var cornerBottomRight: Number = 0
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
        all?.let { cornerTopLeft = it; cornerTopRight = it; cornerBottomLeft = it; cornerBottomRight = it }
        left?.let { cornerTopLeft = it; cornerBottomLeft = it }
        right?.let { cornerTopRight = it; cornerBottomRight = it }
        top?.let { cornerTopLeft = it; cornerTopRight = it }
        bottom?.let { cornerBottomLeft = it; cornerBottomRight = it }
        topLeft?.let { cornerTopLeft = it }
        topRight?.let { cornerTopRight = it }
        bottomLeft?.let { cornerBottomLeft = it }
        bottomRight?.let { cornerBottomRight = it }
    }
}

fun rectangleDrawable(builder: CoreRectangleShapeDrawable.() -> Unit): CoreDrawable {
    return CoreRectangleShapeDrawable().apply(builder)
}
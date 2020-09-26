package net.apptronic.core.view.shape

import net.apptronic.core.view.properties.CoreColor

class CoreOvalShapeDrawable internal constructor() : CoreDrawable {
    var fillColor: CoreColor? = null
    var strokeColor: CoreColor? = null
    var strokeWidth: Number? = null
}

fun ovalDrawable(builder: CoreOvalShapeDrawable.() -> Unit): CoreDrawable {
    return CoreOvalShapeDrawable().apply(builder)
}
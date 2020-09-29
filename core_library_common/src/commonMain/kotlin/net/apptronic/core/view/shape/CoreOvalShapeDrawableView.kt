package net.apptronic.core.view.shape

import net.apptronic.core.view.CoreViewBuilder

class CoreOvalShapeDrawableView internal constructor() : CoreShapeView()

fun CoreViewBuilder.ovalShape(builder: CoreOvalShapeDrawableView.() -> Unit): CoreOvalShapeDrawableView {
    return onNextView(CoreOvalShapeDrawableView(), builder)
}
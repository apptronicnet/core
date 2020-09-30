package net.apptronic.core.view.shape

import net.apptronic.core.view.ICoreViewBuilder

class CoreOvalShapeDrawableView internal constructor() : CoreShapeView()

fun ICoreViewBuilder.ovalShape(builder: CoreOvalShapeDrawableView.() -> Unit): CoreOvalShapeDrawableView {
    return onNextView(CoreOvalShapeDrawableView(), builder)
}
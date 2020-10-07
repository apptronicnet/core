package net.apptronic.core.view.shape

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.view.ICoreViewBuilder

@UnderDevelopment
class CoreOvalShapeDrawableView internal constructor() : CoreShapeView()

@UnderDevelopment
fun ICoreViewBuilder.ovalShape(builder: CoreOvalShapeDrawableView.() -> Unit): CoreOvalShapeDrawableView {
    return onNextView(CoreOvalShapeDrawableView(), builder)
}
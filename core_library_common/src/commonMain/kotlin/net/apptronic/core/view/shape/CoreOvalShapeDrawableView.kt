package net.apptronic.core.view.shape

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext

class CoreOvalShapeDrawableView internal constructor(context: CoreViewContext) : CoreShapeView(context)

fun CoreViewBuilder.ovalShape(): ICoreView {
    return nextView(::CoreOvalShapeDrawableView)
}
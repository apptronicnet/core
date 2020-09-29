package net.apptronic.core.view

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext

class EmptyView(context: CoreViewContext) : CoreView(context)

fun CoreViewBuilder.emptyView(builder: ICoreView.() -> Unit = {}): ICoreView {
    return nextView(::EmptyView, builder)
}
package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ICoreViewBuilder

class EmptyView internal constructor() : CoreView()

fun ICoreViewBuilder.emptyView(builder: ICoreView.() -> Unit = {}): EmptyView {
    return onNextView(EmptyView(), builder)
}
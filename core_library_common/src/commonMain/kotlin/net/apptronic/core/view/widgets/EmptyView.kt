package net.apptronic.core.view.widgets

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ICoreViewBuilder

@UnderDevelopment
class EmptyView internal constructor() : CoreView()

@UnderDevelopment
fun ICoreViewBuilder.emptyView(builder: ICoreView.() -> Unit = {}): EmptyView {
    return onNextView(EmptyView(), builder)
}
package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.CoreViewBuilder
import net.apptronic.core.view.ICoreView

class EmptyView internal constructor() : CoreView()

fun CoreViewBuilder.emptyView(builder: ICoreView.() -> Unit = {}): EmptyView {
    return onNextView(EmptyView(), builder)
}
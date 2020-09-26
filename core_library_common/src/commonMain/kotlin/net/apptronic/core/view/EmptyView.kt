package net.apptronic.core.view

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration

class EmptyView(viewConfiguration: ViewConfiguration) : BaseCoreView(viewConfiguration)

fun CoreViewBuilder.emptyView(builder: CoreView.() -> Unit = {}) {
    nextView(EmptyView(viewConfiguration), builder)
}
package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext

open class CoreSpacerView internal constructor(context: CoreViewContext) : CoreView(context)

fun CoreViewBuilder.spacerView(builder: CoreSpacerView.() -> Unit = {}): CoreView {
    return nextView(::CoreSpacerView, builder)
}
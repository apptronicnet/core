package net.apptronic.core.view.widgets

import net.apptronic.core.view.BaseCoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration

open class CoreSpacerView internal constructor(viewConfiguration: ViewConfiguration) : BaseCoreView(viewConfiguration)

fun CoreViewBuilder.spacerView(builder: CoreSpacerView.() -> Unit = {}) {
    nextView(CoreSpacerView(viewConfiguration), builder)
}
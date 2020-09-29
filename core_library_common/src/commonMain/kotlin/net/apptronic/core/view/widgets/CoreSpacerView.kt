package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.CoreViewBuilder

open class CoreSpacerView internal constructor() : CoreView()

fun CoreViewBuilder.spacerView(builder: CoreSpacerView.() -> Unit = {}): CoreView {
    return onNextView(CoreSpacerView(), builder)
}
package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ICoreViewBuilder

open class CoreSpacerView internal constructor() : CoreView()

fun ICoreViewBuilder.spacerView(builder: CoreSpacerView.() -> Unit = {}): CoreView {
    return onNextView(CoreSpacerView(), builder)
}
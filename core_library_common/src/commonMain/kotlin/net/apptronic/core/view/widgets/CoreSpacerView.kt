package net.apptronic.core.view.widgets

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ICoreViewBuilder

@UnderDevelopment
open class CoreSpacerView internal constructor() : CoreView()

@UnderDevelopment
fun ICoreViewBuilder.spacerView(builder: CoreSpacerView.() -> Unit = {}): CoreView {
    return onNextView(CoreSpacerView(), builder)
}
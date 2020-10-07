package net.apptronic.core.view.widgets

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ICoreViewBuilder
import net.apptronic.core.view.commons.ICoreButtonView
import net.apptronic.core.view.containers.CoreContainerView
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

@UnderDevelopment
open class CoreContainerButtonView internal constructor() : CoreContainerView(), ICoreButtonView {

    override var onClick = viewProperty({})

    override var onLongClick = viewProperty({})

    override var contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override var isEnabled = viewProperty(true)

}

@UnderDevelopment
fun ICoreViewBuilder.buttonContainerView(builder: CoreContainerButtonView.() -> Unit): CoreView {
    return onNextView(CoreContainerButtonView(), builder)
}
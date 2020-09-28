package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.container.CoreContainerView
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.widgets.commons.ICoreButtonView

open class CoreContainerButtonView(context: CoreViewContext) : CoreContainerView(context), ICoreButtonView {

    override var onClick = viewProperty({})

    override var onLongClick = viewProperty({})

    override var contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override var isEnabled = viewProperty(true)

}

fun CoreViewBuilder.buttonContainerView(builder: CoreContainerButtonView.() -> Unit): CoreView {
    return nextView(::CoreContainerButtonView, builder)
}
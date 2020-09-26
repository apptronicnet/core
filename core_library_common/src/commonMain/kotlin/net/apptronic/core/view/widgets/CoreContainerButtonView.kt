package net.apptronic.core.view.widgets

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.container.BaseCoreContainerView
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.widgets.commons.ICoreButtonView

open class CoreContainerButtonView(viewConfiguration: ViewConfiguration) : BaseCoreContainerView(viewConfiguration), ICoreButtonView {

    override var onClick = viewProperty<() -> Any>({})

    override var onLongClick = viewProperty<() -> Any>({})

    override var contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override var isEnabled = viewProperty(true)

}

fun CoreViewBuilder.buttonContainerView(builder: CoreContainerButtonView.() -> Unit) {
    nextView(CoreContainerButtonView(viewConfiguration), builder)
}
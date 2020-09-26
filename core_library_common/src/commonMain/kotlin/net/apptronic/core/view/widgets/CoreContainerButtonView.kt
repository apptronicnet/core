package net.apptronic.core.view.widgets

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.container.BaseCoreContainerView
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

open class CoreContainerButtonView(viewConfiguration: ViewConfiguration) : BaseCoreContainerView(viewConfiguration), CoreButton {

    override var onClick: () -> Any = {}

    override var onLongClick: () -> Any = {}

    override var contentAlignmentHorizontal: HorizontalAlignment = DefaultAlignment

    override var contentAlignmentVertical: VerticalAlignment = DefaultAlignment

    override var isEnabled = viewProperty(true)

}

fun CoreViewBuilder.buttonContainerView(builder: CoreContainerButtonView.() -> Unit) {
    nextView(CoreContainerButtonView(viewConfiguration).also(builder))
}
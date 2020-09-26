package net.apptronic.core.view.container

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.layer.CoreViewLayer
import net.apptronic.core.view.properties.LayoutOrientation

open class StackCoreContainerView internal constructor(
        viewConfiguration: ViewConfiguration
) : BaseCoreContainerView(viewConfiguration), OrientationContainerView, SpacingContainerView, DividerContainerView {

    override var orientation: LayoutOrientation = LayoutOrientation.Horizontal

    override var divider: CoreViewLayer? = null

    override var dividerHeight = viewProperty<Number>(0)

    override var spacing = viewProperty<Number>(0)

}

fun CoreViewBuilder.stackView(builder: StackCoreContainerView.() -> Unit) {
    nextView(StackCoreContainerView(viewConfiguration).also(builder))
}
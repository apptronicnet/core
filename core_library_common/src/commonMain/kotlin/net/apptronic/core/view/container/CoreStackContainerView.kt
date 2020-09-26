package net.apptronic.core.view.container

import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.container.commons.ICoreOrientationContainerView
import net.apptronic.core.view.container.commons.IDividerContainerView
import net.apptronic.core.view.container.commons.ISpacingContainerView
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreDimensionZero
import net.apptronic.core.view.layer.CoreViewLayer
import net.apptronic.core.view.properties.LayoutOrientation

/**
 * View container in which all views are placed one after another
 */
open class CoreStackContainerView internal constructor(
        viewConfiguration: ViewConfiguration
) : BaseCoreContainerView(viewConfiguration), ICoreOrientationContainerView, ISpacingContainerView, IDividerContainerView {

    override val orientation = ViewProperty<LayoutOrientation>(LayoutOrientation.Horizontal)

    override val divider = viewProperty<CoreViewLayer?>(null)

    override val dividerSize = viewProperty<CoreDimension>(CoreDimensionZero)

    override val spacing = viewProperty<CoreDimension>(CoreDimensionZero)

    override val dividerOnTop = viewProperty(false)

    override val dividerOnBottom = viewProperty(false)

}

fun CoreViewBuilder.stackContainer(builder: CoreStackContainerView.() -> Unit) {
    nextView(CoreStackContainerView(viewConfiguration), builder)
}
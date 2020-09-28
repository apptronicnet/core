package net.apptronic.core.view.container

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.container.commons.ICoreOrientationView
import net.apptronic.core.view.container.commons.IDividerContainerView
import net.apptronic.core.view.container.commons.ISpacingContainerView
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreDimensionZero
import net.apptronic.core.view.properties.LayoutOrientation

/**
 * View container in which all views are placed one after another
 */
open class CoreStackContainerView internal constructor(
        context: CoreViewContext
) : CoreContainerView(context), ICoreOrientationView, ISpacingContainerView, IDividerContainerView {

    override val orientation = viewProperty(LayoutOrientation.Horizontal)

    override val divider = viewProperty<ICoreView?>(null)

    override val spacing = viewProperty<CoreDimension>(CoreDimensionZero)

    override val dividerOnTop = viewProperty(false)

    override val dividerOnBottom = viewProperty(false)

}

fun CoreViewBuilder.stackContainer(builder: CoreStackContainerView.() -> Unit): ICoreView {
    return nextView(::CoreStackContainerView, builder)
}
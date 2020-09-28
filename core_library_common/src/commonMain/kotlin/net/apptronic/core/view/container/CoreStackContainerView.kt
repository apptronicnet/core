package net.apptronic.core.view.container

import net.apptronic.core.component.value
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.container.commons.ICoreOrientationContainerView
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
) : CoreContainerView(context), ICoreOrientationContainerView, ISpacingContainerView, IDividerContainerView {

    override val orientation = value<LayoutOrientation>(LayoutOrientation.Horizontal)

    override val divider = value<ICoreView?>(null)

    override val spacing = value<CoreDimension>(CoreDimensionZero)

    override val dividerOnTop = value(false)

    override val dividerOnBottom = value(false)

}

fun CoreViewBuilder.stackContainer(builder: CoreStackContainerView.() -> Unit): ICoreView {
    return nextView(::CoreStackContainerView, builder)
}
package net.apptronic.core.view.containers

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ICoreViewBuilder
import net.apptronic.core.view.containers.commons.ICoreOrientationView
import net.apptronic.core.view.containers.commons.IDividerContainerView
import net.apptronic.core.view.containers.commons.ISpacingContainerView
import net.apptronic.core.view.properties.LayoutOrientation

/**
 * View container in which all views are placed one after another
 */
open class CoreStackContainerView internal constructor()
    : CoreContainerView(), ICoreOrientationView, ISpacingContainerView, IDividerContainerView {

    override val orientation = viewProperty(LayoutOrientation.Horizontal)

    override val divider = viewProperty<ICoreView?>(null)

    override val spacing = viewProperty<Number>(0)

    override val dividerOnTop = viewProperty(false)

    override val dividerOnBottom = viewProperty(false)

}

fun ICoreViewBuilder.stackContainer(builder: CoreStackContainerView.() -> Unit): ICoreView {
    return onNextView(CoreStackContainerView(), builder)
}
package net.apptronic.core.view.container

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.container.commons.ICoreOrientationContainerView
import net.apptronic.core.view.container.commons.IDividerContainerView
import net.apptronic.core.view.container.commons.ISpacingContainerView
import net.apptronic.core.view.layer.CoreViewLayer
import net.apptronic.core.view.properties.LayoutOrientation

/**
 * View container in which all views are placed one after another
 */
open class CoreStackContainerView internal constructor(
        viewConfiguration: ViewConfiguration
) : BaseCoreContainerView(viewConfiguration), ICoreOrientationContainerView, ISpacingContainerView, IDividerContainerView {

    override var orientation: LayoutOrientation = LayoutOrientation.Horizontal

    override var divider: CoreViewLayer? = null

    override var dividerHeight = viewProperty<Number>(0)

    override var spacing = viewProperty<Number>(0)

}

fun CoreViewBuilder.stackContainer(builder: CoreStackContainerView.() -> Unit) {
    nextView(CoreStackContainerView(viewConfiguration), builder)
}
package net.apptronic.core.view.container

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

/**
 * Base class for all containers which can hold mutiple views
 */
abstract class CoreContainerView(context: CoreViewContext) : CoreView(context), ICoreContainerView {

    private val children = mutableListOf<ICoreView>()

    override var contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override fun getChildren(): List<ICoreView> {
        return children
    }

    final override fun <T : ICoreView> nextView(constructor: (CoreViewContext) -> T, builder: T.() -> Unit): T {
        return super.nextView(constructor, builder).also {
            children.add(it)
        }
    }

}
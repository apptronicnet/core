package net.apptronic.core.view.containers

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.CoreViewBuilder
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

/**
 * Base class for all containers which can hold mutiple views
 */
abstract class CoreContainerView : CoreView(), ICoreContainerView, CoreViewBuilder {

    override val children: ViewProperty<List<ICoreView>> = viewProperty(emptyList())

    override var contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    private fun addChild(child: ICoreView) {
        val current = children.get()
        val next = mutableListOf<ICoreView>().apply {
            addAll(current)
            add(child)
        }
        children.set(next)
    }

    override fun <T : ICoreView> onNextView(coreView: T, builder: T.() -> Unit): T {
        return super<ICoreContainerView>.onNextView(coreView, builder).also {
            addChild(it)
        }
    }

}
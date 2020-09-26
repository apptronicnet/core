package net.apptronic.core.view.container

import net.apptronic.core.view.BaseCoreView
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.CoreViewStyle
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.container.commons.ICoreContainerView
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

/**
 * Base class for all containers which can hold mutiple views
 */
abstract class BaseCoreContainerView(viewConfiguration: ViewConfiguration) : BaseCoreView(viewConfiguration), ICoreContainerView {

    private var themes = mutableListOf<CoreViewStyle>()

    private val children = mutableListOf<CoreView>()

    override var contentAlignmentHorizontal: HorizontalAlignment = DefaultAlignment

    override var contentAlignmentVertical: VerticalAlignment = DefaultAlignment

    override fun getChildren(): List<CoreView> {
        return children
    }

    override fun theme(vararg theme: CoreViewStyle) {
        super<BaseCoreView>.theme(*theme)
        themes.addAll(theme)
        children.forEach {
            it.theme(*theme)
        }
    }

    override fun <T : CoreView> nextView(child: T, builder: T.() -> Unit) {
        child.theme(*themes.toTypedArray())
        child.apply(builder)
        children.add(child)
    }

    override fun recycle() {
        super<BaseCoreView>.recycle()
        children.forEach {
            it.recycle()
        }
    }

}
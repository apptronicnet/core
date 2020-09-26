package net.apptronic.core.view.container

import net.apptronic.core.view.BaseCoreView
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

open class BaseCoreContainerView(viewConfiguration: ViewConfiguration) : BaseCoreView(viewConfiguration), CoreContainerView {

    private val children = mutableListOf<CoreView>()

    override var contentAlignmentHorizontal: HorizontalAlignment = DefaultAlignment

    override var contentAlignmentVertical: VerticalAlignment = DefaultAlignment

    override fun getChildren(): List<CoreView> {
        return children
    }

    override fun nextView(child: CoreView) {
        children.add(child)
    }

    override fun recycle() {
        super<BaseCoreView>.recycle()
        children.forEach {
            it.recycle()
        }
    }

}
package net.apptronic.core.view.container

import net.apptronic.core.view.CoreContentView
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

interface CoreContainerView : CoreView, CoreContentView, CoreViewBuilder {

    override var contentAlignmentHorizontal: HorizontalAlignment

    override var contentAlignmentVertical: VerticalAlignment

    fun getChildren(): List<CoreView>

}
package net.apptronic.core.view

import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

interface CoreContentView : ICoreView {

    val contentAlignmentVertical: Value<VerticalAlignment>
    val contentAlignmentHorizontal: Value<HorizontalAlignment>

    fun contentAlignment(vertical: VerticalAlignment) {
        contentAlignmentVertical.set(vertical)
    }

    fun contentAlignment(horizontal: HorizontalAlignment) {
        contentAlignmentHorizontal.set(horizontal)
    }

    fun contentAlignment(horizontal: HorizontalAlignment, vertical: VerticalAlignment) {
        contentAlignmentHorizontal.set(horizontal)
        contentAlignmentVertical.set(vertical)
    }

}
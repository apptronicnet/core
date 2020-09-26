package net.apptronic.core.view

import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

interface CoreContentView : CoreView {

    val contentAlignmentVertical: ViewProperty<VerticalAlignment>
    val contentAlignmentHorizontal: ViewProperty<HorizontalAlignment>

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
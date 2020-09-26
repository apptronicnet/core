package net.apptronic.core.view

import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

interface CoreContentView : CoreView {

    var contentAlignmentVertical: VerticalAlignment
    var contentAlignmentHorizontal: HorizontalAlignment

    fun contentAlignment(vertical: VerticalAlignment) {
        contentAlignmentVertical = vertical
    }

    fun contentAlignment(horizontal: HorizontalAlignment) {
        contentAlignmentHorizontal = horizontal
    }

    fun contentAlignment(horizontal: HorizontalAlignment, vertical: VerticalAlignment) {
        contentAlignmentHorizontal = horizontal
        contentAlignmentVertical = vertical
    }

}
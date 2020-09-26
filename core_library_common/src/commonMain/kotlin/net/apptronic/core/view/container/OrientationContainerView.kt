package net.apptronic.core.view.container

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.properties.LayoutOrientation
import net.apptronic.core.view.widgets.CoreSpacerView
import net.apptronic.core.view.widgets.spacerView

interface OrientationContainerView : CoreViewBuilder {

    var orientation: LayoutOrientation

    fun orientation(orientation: LayoutOrientation) {
        this.orientation = orientation
    }

    val Vertical: LayoutOrientation
        get() = LayoutOrientation.Vertical
    val Horizontal: LayoutOrientation
        get() = LayoutOrientation.Horizontal
    val VerticalReversed: LayoutOrientation
        get() = LayoutOrientation.VerticalReversed
    val HorizontalReversed: LayoutOrientation
        get() = LayoutOrientation.HorizontalReversed

    fun spacerView(size: Number, builder: CoreSpacerView.() -> Unit = {}) {
        spacerView {
            when {
                orientation == Horizontal || orientation == HorizontalReversed -> width(size)
                orientation == Vertical || orientation == VerticalReversed -> height(size)
            }
            builder()
        }
    }

}



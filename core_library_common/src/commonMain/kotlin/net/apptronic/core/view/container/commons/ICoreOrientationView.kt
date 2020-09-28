package net.apptronic.core.view.container.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference
import net.apptronic.core.view.properties.LayoutOrientation
import net.apptronic.core.view.widgets.CoreSpacerView
import net.apptronic.core.view.widgets.spacerView

/**
 * Base class for containers which supports orientation and adding spacers according to it's orientation
 */
interface ICoreOrientationView : ICoreView, CoreViewBuilder {

    val orientation: ViewProperty<LayoutOrientation>

    fun orientation(value: LayoutOrientation) {
        orientation.set(value)
    }

    fun orientation(source: Entity<LayoutOrientation>) {
        orientation.set(orientation)
    }

    fun orientation(value: DynamicReference<LayoutOrientation>) {
        orientation.set(value)
    }

    fun orientation(source: DynamicEntityReference<LayoutOrientation, Entity<LayoutOrientation>>) {
        orientation.set(orientation)
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
                orientation.get() == Horizontal || orientation.get() == HorizontalReversed -> width(size)
                orientation.get() == Vertical || orientation.get() == VerticalReversed -> height(size)
            }
            builder()
        }
    }

}



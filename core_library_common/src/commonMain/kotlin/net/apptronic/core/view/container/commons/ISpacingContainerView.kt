package net.apptronic.core.view.container.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference
import net.apptronic.core.view.dimension.CoreDimension

/**
 * Base interface for view which can divide content with standard spacing
 */
interface ISpacingContainerView : ICoreView {

    val spacing: ViewProperty<CoreDimension>

    fun spacing(value: Number) {
        spacing.setDimension(value)
    }

    fun spacing(source: Entity<Number>) {
        spacing.setDimension(source)
    }

    fun spacing(value: DynamicReference<Number>) {
        spacing.setDimension(value)
    }

    fun spacing(source: DynamicEntityReference<Number, Entity<Number>>) {
        spacing.setDimension(source)
    }

}
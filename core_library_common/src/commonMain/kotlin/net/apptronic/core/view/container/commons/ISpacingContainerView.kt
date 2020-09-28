package net.apptronic.core.view.container.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.setDimension

/**
 * Base interface for view which can divide content with standard spacing
 */
interface ISpacingContainerView : ICoreView {

    val spacing: Value<CoreDimension>

    fun spacing(value: Number) {
        spacing.setDimension(value)
    }

    fun spacing(source: Entity<Number>) {
        spacing.setDimension(source)
    }

}
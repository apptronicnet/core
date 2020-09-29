package net.apptronic.core.view.containers.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty

/**
 * Base interface for view which can divide content with standard spacing
 */
interface ISpacingContainerView : ICoreView {

    val spacing: ViewProperty<Number>

    fun spacing(value: Number) {
        spacing.set(value)
    }

    fun spacing(source: Entity<Number>) {
        spacing.set(source)
    }

}
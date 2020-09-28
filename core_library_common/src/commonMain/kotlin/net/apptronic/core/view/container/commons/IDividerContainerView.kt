package net.apptronic.core.view.container.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.view.shape.rectangleShape

/**
 * Base interface for view which can divide content with dividers
 */
interface IDividerContainerView : ICoreView {

    val divider: Value<ICoreView?>

    val dividerOnTop: Value<Boolean>

    val dividerOnBottom: Value<Boolean>

    fun divider(color: CoreColor) {
        divider.set(detachedViewBuilder.rectangleShape {

        })
    }

    fun dividerOnTop(value: Boolean) {
        dividerOnTop.set(value)
    }

    fun dividerOnTop(source: Entity<Boolean>) {
        dividerOnTop.setAs(source)
    }

    fun dividerOnBottom(value: Boolean) {
        dividerOnBottom.set(value)
    }

    fun dividerOnBottom(source: Entity<Boolean>) {
        dividerOnBottom.setAs(source)
    }

    fun divider(builder: CoreViewBuilder.() -> ICoreView) {
        divider.set(detachedViewBuilder.builder())
    }

}
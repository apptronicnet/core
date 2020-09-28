package net.apptronic.core.view.container.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference

/**
 * Base interface for view which can divide content with dividers
 */
interface IDividerContainerView : ICoreView {

    val divider: ViewProperty<ICoreView?>

    val dividerOnTop: ViewProperty<Boolean>

    val dividerOnBottom: ViewProperty<Boolean>

    fun dividerOnTop(value: Boolean) {
        dividerOnTop.set(value)
    }

    fun dividerOnTop(source: Entity<Boolean>) {
        dividerOnTop.set(source)
    }

    fun dividerOnTop(value: DynamicReference<Boolean>) {
        dividerOnTop.set(value)
    }

    fun dividerOnTop(source: DynamicEntityReference<Boolean, Entity<Boolean>>) {
        dividerOnTop.set(source)
    }

    fun dividerOnBottom(value: Boolean) {
        dividerOnBottom.set(value)
    }

    fun dividerOnBottom(source: Entity<Boolean>) {
        dividerOnBottom.set(source)
    }

    fun dividerOnBottom(value: DynamicReference<Boolean>) {
        dividerOnBottom.set(value)
    }

    fun dividerOnBottom(source: DynamicEntityReference<Boolean, Entity<Boolean>>) {
        dividerOnBottom.set(source)
    }

    fun divider(builder: CoreViewBuilder.() -> ICoreView) {
        divider.set(detachedViewBuilder.builder())
    }

}
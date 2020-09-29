package net.apptronic.core.view.containers.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.CoreViewBuilder
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.LayerCoreViewBuilder
import net.apptronic.core.view.ViewProperty

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

    fun dividerOnBottom(value: Boolean) {
        dividerOnBottom.set(value)
    }

    fun dividerOnBottom(source: Entity<Boolean>) {
        dividerOnBottom.set(source)
    }

    fun divider(builder: CoreViewBuilder.() -> ICoreView) {
        divider.set(LayerCoreViewBuilder(this).builder())
    }

}
package net.apptronic.core.view.containers.commons

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.*

/**
 * Base interface for view which can divide content with dividers
 */
@UnderDevelopment
interface IDividerContainerView : ICoreParentView {

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

    fun divider(builder: ICoreViewBuilder.() -> ICoreView) {
        StandaloneCoreViewBuilder(this).builder().also {
            divider.set(it)
        }
    }

}
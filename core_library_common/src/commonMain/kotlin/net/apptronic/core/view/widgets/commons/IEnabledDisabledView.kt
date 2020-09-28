package net.apptronic.core.view.widgets.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference

interface IEnabledDisabledView : ICoreView {

    val isEnabled: ViewProperty<Boolean>

    fun enabled(value: Boolean) {
        isEnabled.set(value)
    }

    fun enabled(source: Entity<Boolean>) {
        isEnabled.set(source)
    }

    fun enabled(value: DynamicReference<Boolean>) {
        isEnabled.set(value)
    }

    fun enabled(source: DynamicEntityReference<Boolean, Entity<Boolean>>) {
        isEnabled.set(source)
    }

}
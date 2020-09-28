package net.apptronic.core.view.widgets.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.view.ICoreView

interface IEnabledDisabledView : ICoreView {

    val isEnabled: Value<Boolean>

    fun enabled(value: Boolean) {
        isEnabled.set(value)
    }

    fun enabled(source: Entity<Boolean>) {
        isEnabled.setAs(source)
    }

}
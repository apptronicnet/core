package net.apptronic.core.view.commons

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty

@UnderDevelopment
interface IEnabledDisabledView : ICoreView {

    val isEnabled: ViewProperty<Boolean>

    fun enabled(value: Boolean) {
        isEnabled.set(value)
    }

    fun enabled(source: Entity<Boolean>) {
        isEnabled.set(source)
    }

}
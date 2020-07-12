package net.apptronic.core.features.provider

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.switchContext
import net.apptronic.core.component.extensions.BaseComponent

abstract class DataProvider<T>(context: Context) : BaseComponent(context) {

    protected abstract val entity: Entity<T>

    fun provide(context: Context): Entity<T> {
        return entity.switchContext(context)
    }

}
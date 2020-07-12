package net.apptronic.core.features.provider

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.extensions.BaseComponent

abstract class DataProvider<T, K>(context: Context, val key: K) : BaseComponent(context) {

    abstract val entity: Entity<T>

}
package net.apptronic.core.commons.dataprovider

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity

abstract class DataProvider<T, K>(context: Context, val key: K) : Component(context) {

    abstract val entity: Entity<T>

}
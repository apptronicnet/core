package net.apptronic.core.commons.dataprovider

import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.Entity

abstract class DataProvider<T, K>(context: Context, val key: K) : Component(context) {

    abstract val entity: Entity<T>

}
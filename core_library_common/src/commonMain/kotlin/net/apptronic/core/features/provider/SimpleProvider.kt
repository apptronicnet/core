package net.apptronic.core.features.provider

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity

class SimpleProvider<T, K>(context: Context, key: K, builder: Component.(K) -> Entity<T>) : Provider<T>(context) {

    override val entity: Entity<T> = builder(key)

}
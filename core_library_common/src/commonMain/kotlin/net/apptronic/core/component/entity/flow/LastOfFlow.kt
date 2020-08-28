package net.apptronic.core.component.entity.flow

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.entity.Entity

fun <T> lastOf(sources: List<Entity<out T>>): Entity<T> {
    return FlowEntity(sources, LastOfFlow())
}

fun <T> lastOf(vararg sources: Entity<out T>): Entity<T> {
    return FlowEntity(sources.toList(), LastOfFlow())
}

class LastOfFlow<T> : FlowEntity.Flow<T> {

    override fun process(sources: List<ValueHolder<T>?>, updatedIndex: Int): ValueHolder<T>? {
        return sources[updatedIndex]
    }

}
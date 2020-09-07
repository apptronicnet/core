package net.apptronic.core.component.entity.composite

import net.apptronic.core.base.collections.Queue
import net.apptronic.core.base.collections.setConsumer
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.entity.Entity

fun <T> lastOf(sources: List<Entity<out T>>): Entity<T> {
    return CompositeEntity(sources, LastOfHandler())
}

fun <T> lastOf(vararg sources: Entity<out T>): Entity<T> {
    return CompositeEntity(sources.toList(), LastOfHandler())
}

class LastOfHandler<T> : ComposeHandler<T, T> {

    override fun compose(queue: Queue<ComposedNext<T>>, count: Int, observer: Observer<T>) {
        queue.setConsumer {
            observer.notify(it.value)

        }
    }

}
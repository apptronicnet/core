package net.apptronic.core.entity.composite

import net.apptronic.core.base.collections.Queue
import net.apptronic.core.base.collections.setConsumer
import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.base.subject.Subject
import net.apptronic.core.entity.Entity

fun <T> lastItemOf(sources: List<Entity<out T>>): Entity<T> {
    return compose(sources, LastItemOfHandler())
}

fun <T> lastItemOf(vararg sources: Entity<out T>): Entity<T> {
    return compose(sources.toList(), LastItemOfHandler())
}

private class LastItemOfHandler<T> : ComposeHandler<T, T> {

    override fun compose(queue: Queue<ComposedNext<T>>, count: Int): Subject<T> {
        val subject = BehaviorSubject<T>()
        queue.setConsumer {
            subject.update(it.value)
        }
        return subject
    }

}
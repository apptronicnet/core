package net.apptronic.core.component.entity.composite

import net.apptronic.core.base.collections.Queue
import net.apptronic.core.base.collections.setConsumer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.entity.Entity

fun <T> allLastOf(sources: List<Entity<out T>>): Entity<List<T>> {
    return compose(sources, AllLastOfHandler())
}

fun <T> allLastOf(vararg sources: Entity<out T>): Entity<List<T>> {
    return compose(sources.toList(), AllLastOfHandler())
}

private class AllLastOfHandler<T> : ComposeHandler<T, List<T>> {

    override fun compose(queue: Queue<ComposedNext<T>>, count: Int): Subject<List<T>> {
        val list = arrayOfNulls<ValueHolder<T>>(count)
        val subject = BehaviorSubject<List<T>>()
        queue.setConsumer {
            list[it.index] = ValueHolder(it.value)
            val next = list.filterNotNull().map { it.value }
            if (next.size == count) {
                subject.notify(next)
            }
        }
        return subject
    }

}
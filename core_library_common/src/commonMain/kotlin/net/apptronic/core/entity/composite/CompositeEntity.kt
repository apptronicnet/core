package net.apptronic.core.entity.composite

import net.apptronic.core.base.collections.queueOf
import net.apptronic.core.base.subject.Subject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.SubjectEntity
import net.apptronic.core.entity.collectContext

fun <E, T> compose(
        sources: List<Entity<out E>>,
        composeHandler: ComposeHandler<E, T>
): Entity<T> {
    return CompositeEntity(sources, composeHandler)
}

private class CompositeEntity<E, T>(
        sources: List<Entity<out E>>,
        private val composeHandler: ComposeHandler<E, T>
) : SubjectEntity<T>() {

    private val sources = sources.toTypedArray()

    override val context: Context = collectContext(*sources.toTypedArray())

    override val subject: Subject<T>

    init {
        val queue = queueOf<ComposedNext<E>>()
        subject = composeHandler.compose(queue, sources.size)
        sources.forEachIndexed { index, entity ->
            entity.subscribe { next ->
                queue.add(ComposedNext(next, index))
            }
        }
    }

}
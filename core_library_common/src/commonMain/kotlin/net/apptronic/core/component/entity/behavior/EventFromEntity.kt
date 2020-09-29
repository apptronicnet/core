package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.collectContext

/**
 * Use current entity as signal source and when it emits any value - send last value from [source]
 */
fun <T> Entity<*>.onNextSend(source: Entity<T>): Entity<T> {
    return EventFromEntity(this, source)
}

fun <T> Entity<*>.onNextSend(source: Entity<T>, action: (T) -> Unit): Entity<T> {
    return onNextSend(source).also {
        it.subscribe(context, action)
    }
}

/**
 * Use current entity as source emit it last value when [signal] emits any item
 */
fun <T> Entity<T>.sendWhen(signal: Entity<*>): Entity<T> {
    return EventFromEntity(signal, this)
}

fun <T> Entity<T>.sendWhen(signal: Entity<*>, action: (T) -> Unit): Entity<T> {
    return sendWhen(signal).also {
        it.subscribe(context, action)
    }
}

private class EventFromEntity<T>(
    signal: Entity<*>,
    source: Entity<T>
) : SubjectEntity<T>() {

    override val context = collectContext(signal, source)
    private var value: ValueHolder<T>? = null
    override val subject = PublishSubject<T>()

    init {
        source.subscribe(context) {
            value = ValueHolder(it)
        }
        signal.subscribe(context) {
            value?.let {
                subject.update(it.value)
            }
        }
    }

}
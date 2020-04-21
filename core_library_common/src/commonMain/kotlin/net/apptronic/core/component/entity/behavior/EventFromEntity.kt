package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.collectContext
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

/**
 * Use current entity as signal source and when it emits any value - send last value from [source]
 */
fun <T> Entity<*>.onNextMap(source: Entity<T>): Entity<T> {
    return EventFromEntity(this, source)
}

fun <T> Entity<*>.onNextMap(source: Entity<T>, action: (T) -> Unit): Entity<T> {
    return onNextMap(source).also {
        it.subscribe(action)
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
        it.subscribe(action)
    }
}

private class EventFromEntity<T>(
    signal: Entity<*>,
    source: Entity<T>
) : Entity<T> {

    override val context = collectContext(signal, source)
    private var value: ValueHolder<T>? = null
    private val subject = ContextSubjectWrapper(context, PublishSubject<T>())

    init {
        source.subscribe {
            value = ValueHolder(it)
        }
        signal.subscribe {
            value?.let {
                subject.update(it.value)
            }
        }
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subject.subscribe(context, observer)
    }

}
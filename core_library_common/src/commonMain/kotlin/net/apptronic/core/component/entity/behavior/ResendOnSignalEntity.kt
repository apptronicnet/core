package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.base.observable.subject.asValueHolder
import net.apptronic.core.base.observable.subject.doIfSet
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.RelayEntity
import net.apptronic.core.component.entity.subscribe

fun <T> Entity<T>.asResendable(): ResendEntity<T> {
    return ResendOnSignalEntity(this)
}

fun <T> Entity<T>.resendWhen(vararg entities: Entity<*>): ResendEntity<T> {
    return asResendable().signalWhen(*entities)
}

interface ResendEntity<T> : EntityValue<T> {

    fun resendSignal()

}

private class ResendOnSignalEntity<T>(wrappedEntity: Entity<T>) : RelayEntity<T>(wrappedEntity), ResendEntity<T> {

    private var lastValue: ValueHolder<T>? = null

    init {
        wrappedEntity.subscribe {
            lastValue = it.asValueHolder()
        }
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return lastValue
    }

    override fun resendSignal() {
        lastValue.doIfSet { value ->
            getObservers().forEach {
                it.notify(value)
            }
        }
    }

}

fun <E : ResendEntity<T>, T> E.signalWhen(vararg entities: Entity<*>): E {
    entities.forEach {
        it.subscribe(context) {
            resendSignal()
        }
    }
    return this
}
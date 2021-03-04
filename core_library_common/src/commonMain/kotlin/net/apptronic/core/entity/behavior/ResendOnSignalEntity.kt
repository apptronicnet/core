package net.apptronic.core.entity.behavior

import net.apptronic.core.base.subject.*
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.base.RelayEntity

fun <T> Entity<T>.asResendable(): ResendEntity<T> {
    return ResendOnSignalEntity(this)
}

fun <T> Entity<T>.resendWhen(vararg entities: Entity<*>): ResendEntity<T> {
    return asResendable().signalWhen(*entities)
}

interface ResendEntity<T> : Property<T> {

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
                it.update(value)
            }
        }
    }

    override fun doIfSet(action: (T) -> Unit) = lastValue.doIfSet(action)

    override fun get() = lastValue.getOrThrow()

    override fun getOr(fallbackValue: T) = lastValue.getOr(fallbackValue)

    override fun getOr(fallbackValueProvider: () -> T) = lastValue.getOr(fallbackValueProvider)

    override fun getOrNull() = lastValue.getOrNull()

    override fun isSet() = lastValue.isSet()

    override fun isNotSet() = !lastValue.isSet()

}

fun <E : ResendEntity<T>, T> E.signalWhen(vararg entities: Entity<*>): E {
    entities.forEach {
        it.subscribe(context) {
            resendSignal()
        }
    }
    return this
}
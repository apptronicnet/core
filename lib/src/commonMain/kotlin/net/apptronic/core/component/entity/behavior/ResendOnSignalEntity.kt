package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

fun <T> Entity<T>.asResendable(): ResendEntity<T> {
    return ResendOnSignalEntity(this)
}

fun <T> Entity<T>.resendWhen(vararg entities: Entity<*>): ResendEntity<T> {
    return asResendable().signalWhen(*entities)
}

interface ResendEntity<T> : EntityValue<T> {

    fun resendSignal()

}

private class ResendOnSignalEntity<T>(
    private val wrappedEntity: Entity<T>
) : ResendEntity<T> {

    private val subject = ContextSubjectWrapper(getContext(), BehaviorSubject<T>())

    init {
        wrappedEntity.subscribe(subject)
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.wrapped.getValue()
    }

    override fun resendSignal() {
        subject.wrapped.getValue()?.let {
            subject.update(it.value)
        }
    }

    override fun getContext(): Context {
        return wrappedEntity.getContext()
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return subject.subscribe(observer)
    }

}

fun <E : ResendEntity<T>, T> E.signalWhen(vararg entities: Entity<*>): E {
    entities.forEach {
        it.subscribe {
            resendSignal()
        }
    }
    return this
}
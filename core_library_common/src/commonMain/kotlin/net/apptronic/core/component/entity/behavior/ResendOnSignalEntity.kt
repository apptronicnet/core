package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.*
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.base.UpdateEntity
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

private class ResendOnSignalEntity<T>(wrappedEntity: Entity<T>) : SubjectEntity<T>(), ResendEntity<T> {

    override val context: Context = wrappedEntity.context
    override val subject = BehaviorSubject<T>()

    init {
        wrappedEntity.subscribe(subject)
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

    override fun resendSignal() {
        subject.getValue()?.let {
            subject.notify(it.value)
        }
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
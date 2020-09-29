package net.apptronic.core.view.binder

import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.SubjectEntity

/**
 * Specifies dynamically reading property from target type
 */
interface DynamicEntityReference<V, E : Entity<V>> {

    fun subscribeWith(context: Context, callback: (E) -> Unit): Subscription

}

internal class DynamicEntityReferenceImpl<T, V, E : Entity<V>>(
        override val context: Context,
        val referenceGetter: T.() -> E
) : SubjectEntity<E>(), DynamicEntityReference<V, E> {

    override val subject: Subject<E> = BehaviorSubject()

    fun read(source: T) {
        subject.update(source.referenceGetter())
    }

    override fun subscribeWith(context: Context, callback: (E) -> Unit): Subscription {
        return subscribe(context, callback)
    }

}
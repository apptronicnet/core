package net.apptronic.core.commons.dataprovider

import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.bindContext

abstract class DataProvider<T, K>(context: Context, val key: K) : Component(context) {

    internal val onNewSubscriberSubject = PublishSubject<Unit>()

    val onNewSubscriber: Entity<Unit> = onNewSubscriberSubject.bindContext(context)

    abstract val entity: Entity<T>

    suspend fun processLoadDataRequest(): T {
        throw UnsupportedOperationException("$this does not support client reload requests")
    }

}
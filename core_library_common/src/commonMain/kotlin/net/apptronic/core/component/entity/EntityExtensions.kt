package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.ObservableEntity
import net.apptronic.core.component.entity.behavior.takeFirst

fun <T> Observable<T>.bindContext(context: Context): Entity<T> {
    return EntityObservableWrapper(context, this)
}

private class EntityObservableWrapper<T>(
        override val context: Context,
        override val observable: Observable<T>
) : ObservableEntity<T>()

/**
 * Take current value of entity and perform action with it. Will do nothing if entity value is not set or if entity
 * not holds it's value.
 */
fun <T> Entity<T>.withCurrent(action: (T) -> Unit) {
    takeFirst().subscribe(action).unsubscribe()
}
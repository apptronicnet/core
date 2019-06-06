package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptionFactory

fun <T> Observable<T>.bindContext(context: Context): Entity<T> {
    return EntityObservableWrapper(context, this)
}

private class EntityObservableWrapper<T>(
        private val context: Context,
        private val observable: Observable<T>
) : Entity<T> {

    private val subscriptionFactory = ContextSubscriptionFactory<T>(context)

    override fun getContext(): Context {
        return context
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subscriptionFactory.using(context).subscribe(observer, observable)
    }

}

fun <T> Entity<T>.subscribe(callback: (T) -> Unit): EntitySubscription {
    return subscribe(object : Observer<T> {
        override fun notify(value: T) {
            callback.invoke(value)
        }
    })
}

fun <T> Entity<T>.subscribe(context: Context, callback: (T) -> Unit): EntitySubscription {
    return subscribe(context, object : Observer<T> {
        override fun notify(value: T) {
            callback.invoke(value)
        }
    })
}

fun <T> ValueHolder<T>?.get(): T {
    if (this != null) {
        return this.value
    } else {
        throw ValueNotSetException()
    }
}


fun <T> ValueHolder<T>?.getOrNull(): T? {
    return this?.value
}

fun <T> ValueHolder<T>?.isSet(): Boolean {
    return this != null
}

fun <T> ValueHolder<T>?.doIfSet(action: (T) -> Unit): Boolean {
    return this?.let {
        action.invoke(it.value)
        true
    } ?: false
}
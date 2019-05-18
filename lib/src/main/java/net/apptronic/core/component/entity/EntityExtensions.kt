package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context

fun Subscription.bindContext(context: Context): EntitySubscription {
    context.getLifecycle().onExitFromActiveStage {
        unsubscribe()
    }
    return EntitySubscriptionImpl(this)
}

private class EntitySubscriptionImpl(
    private val subscription: Subscription
) : EntitySubscription {

    override fun unsubscribe() {
        subscription.unsubscribe()
    }

}

fun <T> Observable<T>.bindContext(context: Context): Entity<T> {
    return EntityObservableWrapper(context, this)
}

private class EntityObservableWrapper<T>(
    private val context: Context,
    private val observable: Observable<T>
) : Entity<T> {

    override fun getContext(): Context {
        return context
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return observable.subscribe(observer).bindContext(context)
    }

}

fun <T> Entity<T>.subscribe(callback: (T) -> Unit): EntitySubscription {
    return subscribe(object : Observer<T> {
        override fun notify(value: T) {
            callback.invoke(value)
        }
    })
}

fun <T> Entity<T>.subscribe(context: Context, observer: Observer<T>): EntitySubscription {
    return subscribe(observer).also { subscription ->
        context.getLifecycle().onExitFromActiveStage {
            subscription.unsubscribe()
        }
    }
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
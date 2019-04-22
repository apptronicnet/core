package net.apptronic.core.component.entity

import net.apptronic.core.component.context.Context

fun <T> Predicate<T>.subscribe(callback: (T) -> Unit): Subscription {
    return subscribe(object : PredicateObserver<T> {
        override fun notify(value: T) {
            callback.invoke(value)
        }
    })
}

fun <T> Predicate<T>.subscribe(context: Context, observer: PredicateObserver<T>): Subscription {
    return subscribe(observer).also { subscription ->
        val stage = context.getLifecycle().getActiveStage()
        if (stage != null) {
            stage.doOnExit {
                subscription.unsubscribe()
            }
        } else {
            subscription.unsubscribe()
        }
    }
}

fun <T> Predicate<T>.subscribe(context: Context, callback: (T) -> Unit): Subscription {
    return subscribe(context, object : PredicateObserver<T> {
        override fun notify(value: T) {
            callback.invoke(value)
        }
    })
}
package net.apptronic.core.component.entity

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.coroutines.serial
import net.apptronic.core.component.entity.base.ObservableEntity

fun <T> Observable<T>.bindContext(context: Context): Entity<T> {
    return EntityObservableWrapper(context, this)
}

private class EntityObservableWrapper<T>(
        override val context: Context,
        override val observable: Observable<T>
) : ObservableEntity<T>()

fun <T> Entity<T>.subscribe(callback: (T) -> Unit): EntitySubscription {
    return subscribe(object : Observer<T> {
        override fun notify(value: T) {
            callback.invoke(value)
        }
    })
}

fun <T> Entity<T>.subscribeSuspend(callback: suspend CoroutineScope.(T) -> Unit): EntitySubscription {
    val coroutineLauncher = context.coroutineLauncherScoped().serial()
    return subscribe(object : Observer<T> {
        override fun notify(value: T) {
            coroutineLauncher.launch {
                callback(value)
            }
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

fun <T> Entity<T>.subscribeSuspend(context: Context, callback: suspend CoroutineScope.(T) -> Unit): EntitySubscription {
    val coroutineLauncher = context.coroutineLauncherScoped().serial()
    return subscribe(context, object : Observer<T> {
        override fun notify(value: T) {
            coroutineLauncher.launch {
                callback(value)
            }
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
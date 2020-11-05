package net.apptronic.core.entity

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.context.coroutines.serialThrottler
import net.apptronic.core.context.lifecycle.Lifecycle

/**
 * Entity is [Observable] which is bound to [Context] and automatically works with it's [Lifecycle].
 */
interface Entity<T> : Observable<T> {

    val context: Context

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return subscribe(context, observer)
    }

    fun subscribe(targetContext: Context, observer: Observer<T>): EntitySubscription

    override fun subscribe(callback: (T) -> Unit): EntitySubscription {
        return subscribe(object : Observer<T> {
            override fun notify(value: T) {
                callback.invoke(value)
            }
        })
    }

    fun subscribeSuspend(callback: suspend CoroutineScope.(T) -> Unit): EntitySubscription {
        val coroutineThrottler = context.lifecycleCoroutineScope.serialThrottler()
        return subscribe(object : Observer<T> {
            override fun notify(value: T) {
                coroutineThrottler.launch {
                    callback(value)
                }
            }
        })
    }

    fun subscribe(context: Context, callback: (T) -> Unit): EntitySubscription {
        return subscribe(context, object : Observer<T> {
            override fun notify(value: T) {
                callback.invoke(value)
            }
        })
    }

    fun subscribeSuspend(context: Context, callback: suspend CoroutineScope.(T) -> Unit): EntitySubscription {
        val coroutineThrottler = context.lifecycleCoroutineScope.serialThrottler()
        return subscribe(context, object : Observer<T> {
            override fun notify(value: T) {
                coroutineThrottler.launch {
                    callback(value)
                }
            }
        })
    }

    fun switchContext(targetContext: Context): Entity<T> {
        return if (this.context != targetContext) {
            ContextSwitchEntity(this, targetContext)
        } else {
            this
        }
    }

}


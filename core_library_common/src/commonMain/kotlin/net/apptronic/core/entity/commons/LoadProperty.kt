package net.apptronic.core.entity.commons

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.behavior.delay
import net.apptronic.core.entity.function.not
import net.apptronic.core.entity.function.or

fun <T> IComponent.genericLoadProperty(lazy: Boolean = false, delay: Long = 0L, loadFunction: suspend CoroutineScope.(Unit) -> T): LoadProperty<Unit, T> {
    return LoadProperty<Unit, T>(context, delay, loadFunction).also { property ->
        property.setLazy(lazy)
        property.reload(Unit)
    }
}

fun <R, T> IComponent.loadProperty(lazy: Boolean = false, delay: Long = 0L, loadFunction: suspend CoroutineScope.(R) -> T): LoadProperty<R, T> {
    return LoadProperty<R, T>(context, delay, loadFunction).also { property ->
        property.setLazy(lazy)
    }
}

fun <R, T> IComponent.loadProperty(requestSource: Entity<R>, lazy: Boolean = false, delay: Long = 0L, loadFunction: suspend CoroutineScope.(R) -> T): LoadProperty<R, T> {
    return LoadProperty<R, T>(context, delay, loadFunction).also { property ->
        property.setLazy(lazy)
        requestSource.subscribe {
            property.reload(it)
        }
    }
}

/**
 * [SimpleProperty] which loads it's value from [loadFunction].
 *
 * This is useful when source for data is not another [Entity] but some external resource like database, file or remote
 * network API. In this case it is possible to trigger reloading of data when needed.
 *
 * @param delay added after each successful or failed loading before getting next request to prevent too frequent
 * refreshing of data.
 */
class LoadProperty<R, T>(
        context: Context,
        private val delay: Long = 0L,
        private val loadFunction: suspend CoroutineScope.(R) -> T
) : SimpleProperty<T>(context) {

    private val coroutineScope = context.lifecycleCoroutineScope

    /**
     * Defines is adding new subscriber should trigger reload or not
     */
    var reloadOnSubscribe = false

    /**
     * Set error handler for [loadFunction]. Default handler will cause crash when any [Exception] thrown
     * from [loadFunction].
     */
    var onError: (Exception) -> Unit = {
        throw it
    }

    private val isLazy = SimpleMutableValue<Boolean>(context).also { it.set(true) }
    private val haveObservers = SimpleMutableValue<Boolean>(context).also { it.set(false) }
    private val canLoad = (isLazy.not() or haveObservers).asProperty()

    private var scheduleReload = false
    private var isReloading = false

    /**
     * Defines is [LoadProperty] should load/reload it's value when no subscribers for it.
     *
     * In case if value was loaded earlier it will be immediately emitted for new subscriber also in case when
     * [isLazy] is set to true.
     */
    fun setLazy(lazy: Boolean) {
        isLazy.set(lazy)
        if (!lazy) {
            reload()
        }
    }

    fun isLazy(): Boolean {
        return isLazy.get()
    }

    override fun onObserversChanged(list: List<Observer<T>>) {
        super.onObserversChanged(list)
        haveObservers.set(list.isNotEmpty())
    }

    private var lastRequest: ValueHolder<R>? = null

    init {
        // trigger reload when in lazy mode and new observers appeared
        haveObservers.subscribe(context) {
            if (it && isLazy.get()) {
                reload()
            }
        }
    }

    fun reload(): Boolean {
        return lastRequest?.let {
            reload(it.value)
            true
        } ?: false
    }

    fun reload(request: R) {
        lastRequest = ValueHolder(request)
        if (!isReloading && canLoad.get()) {
            loadNext()
        } else {
            scheduleReload = true
        }
    }

    private fun loadNext() {
        val request = this.lastRequest
        if (request != null) {
            scheduleReload = false
            isReloading = true
            coroutineScope.launch {
                try {
                    val result = loadFunction(request.value)
                    subject.update(result)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    onError(e)
                }
                if (delay > 0L) {
                    delay(delay)
                }
                isReloading = false
                if (scheduleReload) {
                    loadNext()
                }
            }
        }
    }

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        if (reloadOnSubscribe) {
            reload()
        }
        return super.onNewObserver(targetContext, observer)
    }

}
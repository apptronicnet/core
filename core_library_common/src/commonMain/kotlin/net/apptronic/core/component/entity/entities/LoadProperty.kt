package net.apptronic.core.component.entity.entities

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.Component
import net.apptronic.core.component.asProperty
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.not
import net.apptronic.core.component.entity.functions.or
import net.apptronic.core.component.entity.subscribe

fun <T> Component.genericLoadProperty(lazy: Boolean = false, loadFunction: suspend CoroutineScope.(Unit) -> T): LoadProperty<Unit, T> {
    return LoadProperty<Unit, T>(context, loadFunction).also { property ->
        property.setLazy(lazy)
        property.reload(Unit)
    }
}

fun <R, T> Component.loadProperty(lazy: Boolean = false, loadFunction: suspend CoroutineScope.(R) -> T): LoadProperty<R, T> {
    return LoadProperty<R, T>(context, loadFunction).also { property ->
        property.setLazy(lazy)
    }
}

fun <R, T> Component.loadProperty(requestSource: Entity<R>, lazy: Boolean = false, loadFunction: suspend CoroutineScope.(R) -> T): LoadProperty<R, T> {
    return LoadProperty<R, T>(context, loadFunction).also { property ->
        property.setLazy(lazy)
        requestSource.subscribe {
            property.reload(it)
        }
    }
}

/**
 * [Property] which loads it's value from [loadFunction].
 *
 * This is useful when source for data is not another [Entity] but some external resource like database, file or remote
 * network API. In this case it is possible to trigger reloading of data when needed.
 */
class LoadProperty<R, T>(
        context: Context,
        private val loadFunction: suspend CoroutineScope.(R) -> T
) : Property<T>(context) {

    private val coroutineLauncher = context.coroutineLauncherScoped()

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

    private val isLazy = Value<Boolean>(context).also { it.set(true) }
    private val haveObservers = Value<Boolean>(context).also { it.set(false) }
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
        haveObservers.subscribe {
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
            coroutineLauncher.launch {
                try {
                    val result = loadFunction(request.value)
                    subject.update(result)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    onError(e)
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
package net.apptronic.core.commons.dataprovider

import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.commons.dataprovider.cache.DataProviderCache
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.base.withOnNext
import net.apptronic.core.entity.bindContext
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.exceptions.TryCatchResult
import net.apptronic.core.entity.exceptions.onException

/**
 * Provider some data of type [T] with key [K]. This class encapsulates loading of loading an
 */
abstract class DataProvider<K, T>(context: Context, val key: K) : Component(context) {

    internal val onNewSubscriberSubject = PublishSubject<Unit>()
    internal val dataValue = value<T>()
    internal val errorEvent = typedEvent<Exception>()

    /**
     * Publishes all errors sent to [notifyError]
     */
    val onError: Entity<Exception> = errorEvent

    /**
     * Invoke this method to notify clients about [Exception] during loading data
     */
    fun notifyError(e: Exception) {
        errorEvent.update(e)
        onNotifyError(e)
    }

    /**
     * Action to do when [notifyError] called
     */
    open fun onNotifyError(e: Exception) {
        e.printStackTrace()
    }

    /**
     * Can be used inside [DataProvider] to get current value of data. This value automatically set from
     * [DataProviderCache] if provided, and also from external invocation of [loadData], and also weil be automatically
     * set from [dataProviderEntity].
     */
    val data: Property<T> = dataValue

    /**
     * Here it is possible to do final filtering of data before sending to clients. [source] includes all data
     * from [dataProviderEntity], from [DataProviderCache] and [loadData] invocations
     */
    open val outData: Entity<T> = data

    /**
     * Event emitted when new [DataProviderProperty] started to consume data from this [DataProvider].
     */
    val onNewClient: Entity<Unit> = onNewSubscriberSubject.bindContext(context)

    /**
     * Entity for providing data from [DataProviderProperty]s.
     */
    abstract val dataProviderEntity: Entity<T>

    /**
     * Can be called externally by [DataProviderProperty] to force request data reloading. This method can throw
     * [UnsupportedOperationException] in case if action is not supported of implement data loading. Also it can be
     * used as source for [dataProviderEntity].
     *
     * In case when loading process supports returning null when [DataProvider] does not supports null values
     * [requireNotNull] can be called.
     */
    open suspend fun loadData(): T {
        throw UnsupportedOperationException("$this does not support client reload request")
    }

    /**
     * Will be called if this [DataProvider] is active and [DataProviderDispatcher.resetData] will be called.
     *
     * In case if this [DataProvider] stores any data inside it is time to clear it.
     */
    open fun onClearRequested() {
        // implement by subclasses if needed
    }

    /**
     * This method can be invoked in [loadData] when load result can be null but [DataProvider] does not supports
     * providing null data (for example using filtering etc.)
     */
    fun T?.requireNotNull(): T {
        return this ?: throw DataLoadResultIsNullException("Load data for key $key of $this is null")
    }

    fun <X> Entity<TryCatchResult<X>>.onExceptionNotifyError(): Entity<X> = onException(::notifyError)

    fun Entity<Exception>.withOnNextNotifyError(): Entity<Exception> = withOnNext(::notifyError)

}
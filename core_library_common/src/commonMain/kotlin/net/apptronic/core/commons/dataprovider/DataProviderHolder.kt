package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.commons.dataprovider.cache.DataProviderCache
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.commons.asProperty

internal class DataProviderHolder<K, T>(
        context: Context,
        private val key: K,
        private val dataProvider: DataProvider<K, T>,
        private val cache: DataProviderCache<K, T>?
) : Component(context), Observer<T> {

    private var getCacheJob: Job? = null

    init {
        val cachedValue = cache?.get(key)
        if (cachedValue != null) {
            if (dataProvider.dataValue.isSet().not()) {
                dataProvider.dataValue.set(cachedValue.value)
            }
        } else {
            getCacheJob = cache?.getAsync(key) {
                if (dataProvider.dataValue.isSet().not()) {
                    dataProvider.dataValue.set(it)
                }
            }
        }
        dataProvider.dataProviderEntity.subscribe(context, this)
        dataProvider.data.subscribe {
            cache?.set(key, it)
        }
        doOnTerminate {
            cache?.releaseKey(key)
        }
    }

    override fun update(value: T) {
        getCacheJob?.cancel(CancellationException("Cache not needed as data already loaded"))
        getCacheJob = null
        dataProvider.dataValue.set(value)
    }

    fun provideData(targetContext: Context): Property<T> {
        dataProvider.onNewSubscriberSubject.update(Unit)
        return dataProvider.dataValue.switchContext(targetContext).asProperty()
    }

    fun observeErrors(targetContext: Context): Entity<Exception> {
        return dataProvider.errorEvent.switchContext(targetContext)
    }

    suspend fun executeReloadRequest(): T {
        val value = dataProvider.loadData()
        dataProvider.dataValue.set(value)
        return value
    }

}
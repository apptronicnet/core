package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.commons.cache.CacheComponent
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value

class DataProviderHolder<T, K>(
        context: Context,
        private val key: K,
        private val dataProvider: DataProvider<T, K>,
        private val cache: CacheComponent<T, K>?
) : Component(context), Observer<T> {

    private val data = value<T>()
    private var getCacheJob: Job? = null

    init {
        getCacheJob = cache?.getAsync(key) {
            if (data.isSet().not()) {
                data.set(it)
            }
        }
        dataProvider.entity.subscribe(context, this)
        doOnTerminate {
            cache?.releaseKey(key)
        }
    }

    override fun notify(value: T) {
        getCacheJob?.cancel(CancellationException("Cache not needed"))
        getCacheJob = null
        data.set(value)
        cache?.set(key, value)
    }

    fun provideData(targetContext: Context): Entity<T> {
        return data.switchContext(targetContext)
    }

}
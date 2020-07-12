package net.apptronic.core.features.provider

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.switchContext
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.value
import net.apptronic.core.features.cache.CacheComponent

class DataProviderHolder<T, K>(
        context: Context,
        private val key: K,
        private val dataProvider: DataProvider<T, K>,
        private val cache: CacheComponent<T, K>?
) : BaseComponent(context), Observer<T> {

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
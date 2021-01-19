package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import net.apptronic.core.commons.dataprovider.cache.processedWith
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.di.FactoryScope
import net.apptronic.core.context.di.Scope
import net.apptronic.core.context.lifecycle.defineLifecycle
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.context.lifecycle.lifecycleStage
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.base.withOnNext
import net.apptronic.core.entity.commons.asProperty
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.entity.commons.value

private val DataProviderActive = lifecycleStage("DataProviderActive")

private val DataProviderHolderLifecycle = defineLifecycle {
    addStage(DataProviderActive)
}

fun <K : Any, T : Any> Scope.dataProviderContext(
    descriptor: DataProviderDescriptor<K, T>,
    key: K,
    builder: FactoryScope.(K) -> DataProvider<K, T>
) = scopedContext(DataProviderHolderLifecycle) {
    dependencyModule("DataProviderHolderModule") {
        factory(descriptor.providerInstanceDescriptor) {
            builder(key)
        }
    }
}

internal class DataProviderHolder<K : Any, T : Any>(
    context: Context,
    private val descriptor: DataProviderDescriptor<K, T>,
    private val key: K,
) : Component(context) {

    private val dispatcher = injectDataDispatcher(descriptor)
    private val cache =
        optional(descriptor.cacheDescriptor)?.processedWith(optional(descriptor.cacheProcessorDescriptor))
    private val holderDataValue = value<T>()
    private val errorEvents = typedEvent<Exception>()
    private val onNewSubscriberEvent = genericEvent()
    private var currentDataProvider: DataProvider<K, T>? = null

    init {
        onEnterStage(DataProviderActive) {
            val dataProvider: DataProvider<K, T> = inject(descriptor.providerInstanceDescriptor)
            currentDataProvider = dataProvider
            dataProvider.dataValue.subscribe(holderDataValue)
            dataProvider.errorEvent.subscribe(errorEvents)
            onNewSubscriberEvent.subscribe(dataProvider.onNewSubscriberSubject)

            var getCacheJob: Job? = null
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

            dataProvider.dataProviderEntity.withOnNext {
                getCacheJob?.cancel(CancellationException("Cache is not needed as data already loaded"))
                getCacheJob = null
                cache?.set(key, it)
            }.subscribe(context, dataProvider.dataValue)

            onExit {
                cache?.releaseKey(key)
            }
        }

        dispatcher.observeDropDataRequest(context).subscribe {
            if (dispatcher.isActivated) {
                exitStage(DataProviderActive)
            }
            holderDataValue.clear()
            if (dispatcher.isActivated) {
                enterStage(DataProviderActive)
            }
        }

        dispatcher.observeActivated(context).subscribe {
            if (it) {
                enterStage(DataProviderActive)
            } else {
                exitStage(DataProviderActive)
            }
        }
    }

    fun provideData(targetContext: Context): Property<T> {
        onNewSubscriberEvent.update()
        return holderDataValue.switchContext(targetContext).asProperty()
    }

    fun observeErrors(targetContext: Context): Entity<Exception> {
        return errorEvents.switchContext(targetContext)
    }

    suspend fun executeReloadRequest(): T {
        val value = currentDataProvider?.let {
            withContext(it.contextCoroutineScope.coroutineContext) {
                it.loadData()
            }
        } ?: throw CancellationException("DataProvider is not deactivated")
        cache?.set(key, value)
        holderDataValue.set(value)
        return value
    }

}
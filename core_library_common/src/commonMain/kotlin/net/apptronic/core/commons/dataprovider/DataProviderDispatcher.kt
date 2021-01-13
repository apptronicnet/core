package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.DataProviderCachePersistence
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.commons.asProperty
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.value

fun <K : Any, T : Any> Contextual.injectDataDispatcher(
    descriptor: DataProviderDescriptor<K, T>
): DataProviderDispatcher<K, T> {
    return dependencyProvider.inject(descriptor.dispatcherDescriptor)
}

sealed class DataProviderDropDataRequest

object OnlyData : DataProviderDropDataRequest()
class DataAndCache(internal val withPersistence: Boolean) : DataProviderDropDataRequest()

class DataProviderDispatcher<K : Any, T : Any> internal constructor(
    context: Context,
    private val descriptor: DataProviderDescriptor<K, T>,
) : Component(context) {

    private val cache = optional(descriptor.cacheDescriptor)
    private val persistence = optional(descriptor.persistenceDescriptor)

    private val dropDataRequestEvent = genericEvent()

    private val isActivatedValue = value(true)

    /**
     * Is cache available for [DataProvider]s defined by corresponding [DataProviderDescriptor]
     */
    val isCacheAvailable: Boolean = cache != null

    /**
     * Is cache persistence available for [DataProvider]s defined by corresponding [DataProviderDescriptor]
     */
    val isPersistenceAvailable: Boolean = persistence != null

    /**
     * Clear cache if exists
     *
     * @param clearPersistence is it needed to clear [DataProviderCachePersistence]
     * for [DataProvider]s defined by corresponding [DataProviderDescriptor]
     */
    fun resetCache(clearPersistence: Boolean) {
        if (clearPersistence) {
            persistence?.clear()
        }
        cache?.clear()
    }

    /**
     * Defines is activated state for all [DataProvider] with corresponding [DataProviderDescriptor].
     *
     * If it is not in activated state then [DataProvider]s will not be loaded and do not provide any value,
     * but all existing values will be provided for all [DataProviderProperty]s
     */
    var isActivated: Boolean
        get() = this.isActivatedValue.get()
        set(value) {
            isActivatedValue.set(value)
        }

    fun observeActivated(targetContext: Context): Property<Boolean> {
        return isActivatedValue.switchContext(targetContext).asProperty()
    }

    /**
     * Triggers force terminate all alive [DataProvider]s with corresponding [DataProviderDescriptor].
     * All [DataProvider]s which have active clients will be recreated automatically after this action.
     *
     * Also all new [DataProviderProperty]s will receive no data from [DataProvider] until [DataProvider] load new data.
     */
    fun resetData(request: DataProviderDropDataRequest) {
        if (request is DataAndCache) {
            resetCache(request.withPersistence)
        }
        dropDataRequestEvent.update()
    }

    internal fun observeDropDataRequest(targetContext: Context): Entity<Unit> {
        return dropDataRequestEvent.switchContext(targetContext)
    }

}
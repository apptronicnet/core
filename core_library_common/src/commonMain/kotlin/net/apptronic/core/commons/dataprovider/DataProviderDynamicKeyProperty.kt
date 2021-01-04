package net.apptronic.core.commons.dataprovider

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.terminate
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.BaseProperty
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.entity.commons.value

/**
 * Inject data of type [T] using [descriptor] with key changing from [keySource].
 */
@UnderDevelopment
fun <T : Any, K : Any> Contextual.injectData(
    descriptor: DataProviderDescriptor<K, T>,
    keySource: Entity<K>
): DataProviderProperty<T> {
    return DataProviderDynamicKeyProperty(context, descriptor, keySource)
}

@UnderDevelopment
private class DataProviderDynamicKeyProperty<K : Any, T : Any>(
    context: Context,
    val dataDescriptor: DataProviderDescriptor<K, T>,
    val keySource: Entity<K>
) : BaseProperty<T>(context), DataProviderProperty<T> {

    private var keyContextComponent: KeyContextComponent<K, T>? = null

    override val errors = context.typedEvent<Exception>()

    override val observable = context.value<T>()

    override fun postReload() {
        keyContextComponent?.postReload() ?: throw IllegalArgumentException("[keySource] value is not set")
    }

    override suspend fun reload(): T {
        return keyContextComponent?.reload() ?: throw IllegalArgumentException("[keySource] value is not set")
    }

    init {
        keySource.subscribe { key ->
            keyContextComponent?.terminate()
            keyContextComponent = KeyContextComponent(
                context.childContext(), dataDescriptor, key
            ).apply {
                valuesTo(observable)
                errorsTo(errors)
            }
        }
    }

}

private class KeyContextComponent<K : Any, T : Any>(
    context: Context,
    val dataDescriptor: DataProviderDescriptor<K, T>,
    val key: K,
) : Component(context) {

    private val property = injectData(dataDescriptor, key)

    fun valuesTo(target: Observer<T>) {
        property.subscribe(target)
    }

    fun errorsTo(target: Observer<Exception>) {
        property.errors.subscribe(target)
    }

    fun postReload() {
        property.postReload()
    }

    suspend fun reload(): T {
        return property.reload()
    }

}
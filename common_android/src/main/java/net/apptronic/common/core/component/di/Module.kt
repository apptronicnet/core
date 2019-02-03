package net.apptronic.common.core.component.di

import kotlin.reflect.KClass

internal class ObjectDefinition<T : Any>(
    private val providerFactory: () -> ObjectProvider<T>
) {

    private var recycler: (T) -> Unit = {}

    internal fun getProvider(context: DIContext): ObjectProvider<T> {
        return providerFactory.invoke().also {
            it.recycler = recycler
        }
    }

    internal fun addRecycler(recycler: (T) -> Unit) {
        this.recycler = recycler
    }

}

class ProviderDefinition<T : Any> internal constructor(
    private val objectDefinition: ObjectDefinition<T>
) {

    fun onRecycle(recycler: (T) -> Unit) {
        objectDefinition.addRecycler(recycler)
    }

}

class ModuleDefinition internal constructor() {

    private val definitions = mutableListOf<ObjectDefinition<*>>()

    internal fun buildInstance(context: DIContext): Module {
        val providers: List<ObjectProvider<*>> = definitions.map {
            it.getProvider(context)
        }
        return Module(providers)
    }

    inline fun <reified T : Any> factory(
        name: String = "",
        noinline builder: FactoryContext.() -> T
    ): ProviderDefinition<T> {
        return factory(T::class, name, builder)
    }

    inline fun <reified T : Any> single(
        name: String = "",
        noinline builder: FactoryContext.() -> T
    ): ProviderDefinition<T> {
        return single(T::class, name, builder)
    }

    fun <T : Any> factory(
        clazz: KClass<T>,
        name: String = "",
        builder: FactoryContext.() -> T
    ): ProviderDefinition<T> {
        return addDefinition {
            factoryProvider(ObjectKey(clazz, name), builder)
        }
    }

    fun <T : Any> single(
        clazz: KClass<T>,
        name: String = "",
        builder: FactoryContext.() -> T
    ): ProviderDefinition<T> {
        return addDefinition {
            singleProvider(ObjectKey(clazz, name), builder)
        }
    }

    private fun <T : Any> addDefinition(
        providerFactory: () -> ObjectProvider<T>
    ): ProviderDefinition<T> {
        val definition = ObjectDefinition(providerFactory)
        definitions.add(definition)
        return ProviderDefinition(definition)
    }

}

internal class Module constructor(
    private val providers: List<ObjectProvider<*>>
) {

    fun getProvider(key: ObjectKey): ObjectProvider<*>? {
        return providers.firstOrNull {
            it.key == key
        }
    }

}

fun declareModule(initializer: ModuleDefinition.() -> Unit): ModuleDefinition {
    return ModuleDefinition().apply(initializer)
}
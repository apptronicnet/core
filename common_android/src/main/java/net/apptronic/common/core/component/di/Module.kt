package net.apptronic.common.core.component.di

import kotlin.reflect.KClass

internal class ObjectDefinition<TypeDeclaration : Any>(
    private val providerFactory: () -> ObjectProvider<TypeDeclaration>
) {

    private var recycler: (TypeDeclaration) -> Unit = {}

    internal fun getProvider(context: DIContext): ObjectProvider<TypeDeclaration> {
        return providerFactory.invoke().also {
            it.recycler = recycler
        }
    }

    internal fun addRecycler(recycler: (TypeDeclaration) -> Unit) {
        this.recycler = recycler
    }

}

class ProviderDefinition<TypeDeclaration : Any> internal constructor(
    private val objectDefinition: ObjectDefinition<TypeDeclaration>
) {

    fun onRecycle(recycler: (TypeDeclaration) -> Unit): ProviderDefinition<TypeDeclaration> {
        objectDefinition.addRecycler(recycler)
        return this
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

    inline fun <reified TypeDeclaration : Any> factory(
        name: String = "",
        noinline builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return factory(TypeDeclaration::class, name, builder)
    }

    inline fun <reified TypeDeclaration : Any> single(
        name: String = "",
        noinline builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return single(TypeDeclaration::class, name, builder)
    }

    inline fun <reified TypeDeclaration : Any> cast(
        name: String = ""
    ): ProviderDefinition<TypeDeclaration> {
        return cast(TypeDeclaration::class, name)
    }

    fun <TypeDeclaration : Any> factory(
        clazz: KClass<TypeDeclaration>,
        name: String = "",
        builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            factoryProvider(ObjectKey(clazz, name), builder)
        }
    }

    fun <TypeDeclaration : Any> single(
        clazz: KClass<TypeDeclaration>,
        name: String = "",
        builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            singleProvider(ObjectKey(clazz, name), builder)
        }
    }

    fun <TypeDeclaration : Any> cast(
        clazz: KClass<TypeDeclaration>,
        name: String = ""
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            castProvider<TypeDeclaration>(ObjectKey(clazz, name))
        }
    }

    private fun <TypeDeclaration : Any> addDefinition(
        providerFactory: () -> ObjectProvider<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
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
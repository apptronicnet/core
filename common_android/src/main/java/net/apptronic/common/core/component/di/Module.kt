package net.apptronic.common.core.component.di

import kotlin.reflect.KClass

internal class ObjectDefinition<TypeDeclaration : Any>(
    private val providerFactory: ProviderFactoryMethod<TypeDeclaration>
) {

    private var recyclers = mutableListOf<RecyclerMethod<TypeDeclaration>>()
    private var mappings = mutableListOf<ObjectKey>()

    internal fun getProvider(context: DependencyProvider): ObjectProvider<TypeDeclaration> {
        return providerFactory.invoke().also {
            it.recyclers.addAll(this.recyclers)
            it.addMapping(mappings)
        }
    }

    internal fun addRecycler(recycler: RecyclerMethod<TypeDeclaration>) {
        this.recyclers.add(recycler)
    }

    internal fun addMappings(objectKey: ObjectKey) {
        mappings.add(objectKey)
    }

}

class ProviderDefinition<TypeDeclaration : Any> internal constructor(
    private val objectDefinition: ObjectDefinition<TypeDeclaration>
) {

    fun onRecycle(recycler: (TypeDeclaration) -> Unit): ProviderDefinition<TypeDeclaration> {
        objectDefinition.addRecycler(RecyclerMethod(recycler))
        return this
    }

    inline fun <reified Mapping : Any> addMapping(name: String = "") {
        addMapping(Mapping::class, name)
    }

    fun <Mapping : Any> addMapping(clazz: KClass<Mapping>, name: String = "") {
        objectDefinition.addMappings(ObjectKey(clazz, name))
    }

}

class ModuleDefinition internal constructor() {

    private val definitions = mutableListOf<ObjectDefinition<*>>()

    internal fun buildInstance(context: DependencyProvider): Module {
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
            factoryProvider(ObjectKey(clazz, name), BuilderMethod(builder))
        }
    }

    fun <TypeDeclaration : Any> single(
        clazz: KClass<TypeDeclaration>,
        name: String = "",
        builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            singleProvider(ObjectKey(clazz, name), BuilderMethod(builder))
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
        val definition = ObjectDefinition(ProviderFactoryMethod(providerFactory))
        definitions.add(definition)
        return ProviderDefinition(definition)
    }

}

internal class Module constructor(
    private val providers: List<ObjectProvider<*>>
) {

    fun getProvider(objectKey: ObjectKey): ObjectProvider<*>? {
        return providers.firstOrNull {
            it.isMatch(objectKey)
        }
    }

}

fun declareModule(initializer: ModuleDefinition.() -> Unit): ModuleDefinition {
    return ModuleDefinition().apply(initializer)
}
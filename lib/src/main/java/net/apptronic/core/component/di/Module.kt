package net.apptronic.core.component.di

import kotlin.reflect.KClass

internal class ObjectDefinition<TypeDeclaration>(
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

class ProviderDefinition<TypeDeclaration> internal constructor(
    private val objectDefinition: ObjectDefinition<TypeDeclaration>
) {

    fun onRecycle(recycler: (TypeDeclaration) -> Unit): ProviderDefinition<TypeDeclaration> {
        objectDefinition.addRecycler(RecyclerMethod(recycler))
        return this
    }

    inline fun <reified Mapping : Any> addMapping(descriptor: Descriptor<Mapping>? = null) {
        addMapping(Mapping::class, descriptor)
    }

    fun <Mapping : Any> addMapping(
        clazz: KClass<Mapping>,
        descriptor: Descriptor<Mapping>? = null
    ) {
        objectDefinition.addMappings(objectKey(clazz, descriptor))
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
        descriptor: Descriptor<TypeDeclaration>? = null,
        noinline builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return factory(TypeDeclaration::class, descriptor, builder)
    }

    inline fun <reified TypeDeclaration : Any> single(
        descriptor: Descriptor<TypeDeclaration>? = null,
        noinline builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return single(TypeDeclaration::class, descriptor, builder)
    }

    inline fun <reified TypeDeclaration : Any> cast(
        descriptor: Descriptor<TypeDeclaration>? = null
    ): ProviderDefinition<TypeDeclaration> {
        return cast(TypeDeclaration::class, descriptor)
    }

    fun <TypeDeclaration : Any> factory(
        clazz: KClass<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null,
        builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            factoryProvider(objectKey(clazz, descriptor), BuilderMethod(builder))
        }
    }

    fun <TypeDeclaration : Any> single(
        clazz: KClass<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null,
        builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            singleProvider(objectKey(clazz, descriptor), BuilderMethod(builder))
        }
    }

    fun <TypeDeclaration : Any> cast(
        clazz: KClass<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            castProvider<TypeDeclaration>(objectKey(clazz, descriptor))
        }
    }

    /**
     * Java version of [factory]
     */
    fun <TypeDeclaration> declareFactory(
        clazz: Class<TypeDeclaration>,
        builder: ObjectBuilder<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        return declareFactory(clazz, null, builder)
    }

    /**
     * Java version of [factory]
     */
    fun <TypeDeclaration> declareFactory(
        clazz: Class<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null,
        builder: ObjectBuilder<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            factoryProvider(objectKey(clazz, descriptor), BuilderMethod {
                builder.build(this)
            })
        }
    }

    /**
     * Java version of [single]
     */
    fun <TypeDeclaration> declareSingle(
        clazz: Class<TypeDeclaration>,
        builder: ObjectBuilder<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        return declareSingle(clazz, null, builder)
    }

    /**
     * Java version of [single]
     */
    fun <TypeDeclaration> declareSingle(
        clazz: Class<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null,
        builder: ObjectBuilder<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            singleProvider(objectKey(clazz, descriptor), BuilderMethod {
                builder.build(this)
            })
        }
    }

    /**
     * Java version of [cast]
     */
    fun <TypeDeclaration> declareCast(
        clazz: Class<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        return declareCast(clazz, null)
    }

    /**
     * Java version of [cast]
     */
    fun <TypeDeclaration> declareCast(
        clazz: Class<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>?
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            castProvider<TypeDeclaration>(objectKey(clazz, descriptor))
        }
    }

    private fun <TypeDeclaration> addDefinition(
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

fun declareModule(initializer: ModuleBuilder): ModuleDefinition {
    return ModuleDefinition().apply {
        initializer.build(this)
    }
}

interface ModuleBuilder {

    fun build(definition: ModuleDefinition)

}

interface ObjectBuilder<T> {

    fun build(context: FactoryContext): T

}
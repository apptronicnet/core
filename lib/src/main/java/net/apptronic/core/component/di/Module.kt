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

    inline fun <reified Mapping : Any> addMapping(name: String = "") {
        addMapping(Mapping::class, name)
    }

    fun <Mapping : Any> addMapping(clazz: KClass<Mapping>, name: String = "") {
        objectDefinition.addMappings(objectKey(clazz, name))
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
            factoryProvider(objectKey(clazz, name), BuilderMethod(builder))
        }
    }

    fun <TypeDeclaration : Any> single(
        clazz: KClass<TypeDeclaration>,
        name: String = "",
        builder: FactoryContext.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            singleProvider(objectKey(clazz, name), BuilderMethod(builder))
        }
    }

    fun <TypeDeclaration : Any> cast(
        clazz: KClass<TypeDeclaration>,
        name: String = ""
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            castProvider<TypeDeclaration>(objectKey(clazz, name))
        }
    }

    /**
     * Java version of [factory]
     */
    fun <TypeDeclaration> declareFactory(
        clazz: Class<TypeDeclaration>,
        builder: ObjectBuilder<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        return declareFactory(clazz, "", builder)
    }

    /**
     * Java version of [factory]
     */
    fun <TypeDeclaration> declareFactory(
        clazz: Class<TypeDeclaration>,
        name: String = "",
        builder: ObjectBuilder<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            factoryProvider(objectKey(clazz, name), BuilderMethod {
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
        return declareSingle(clazz, "", builder)
    }

    /**
     * Java version of [single]
     */
    fun <TypeDeclaration> declareSingle(
        clazz: Class<TypeDeclaration>,
        name: String,
        builder: ObjectBuilder<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            singleProvider(objectKey(clazz, name), BuilderMethod {
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
        return declareCast(clazz, "")
    }

    /**
     * Java version of [cast]
     */
    fun <TypeDeclaration> declareCast(
        clazz: Class<TypeDeclaration>,
        name: String
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            castProvider<TypeDeclaration>(objectKey(clazz, name))
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
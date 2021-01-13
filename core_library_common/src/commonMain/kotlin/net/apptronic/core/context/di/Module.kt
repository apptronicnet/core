package net.apptronic.core.context.di

import net.apptronic.core.context.Context
import kotlin.reflect.KClass

internal class ObjectDefinition<TypeDeclaration>(
    private val providerFactory: ProviderFactoryMethod<TypeDeclaration>
) {

    private var mappings = mutableListOf<ObjectKey>()

    internal fun getProvider(context: Context): ObjectProvider<TypeDeclaration> {
        return providerFactory.invoke(context).also {
            it.addMapping(mappings)
        }
    }

    internal fun addMappings(objectKey: ObjectKey) {
        mappings.add(objectKey)
    }

}

class BindDefinition<To : Any>(
    val from: ObjectKey, val to: ObjectKey
)

infix fun <To : Any> KClass<*>.alsoAs(to: KClass<To>) =
    BindDefinition<To>(objectKey(this), objectKey(to))

infix fun <To : Any> DependencyDescriptor<*>.alsoAs(to: KClass<To>) =
    BindDefinition<To>(objectKey(this), objectKey(to))

infix fun <To : Any> KClass<*>.alsoAs(to: DependencyDescriptor<To>) =
    BindDefinition<To>(objectKey(this), objectKey(to))

infix fun <To : Any> DependencyDescriptor<*>.alsoAs(to: DependencyDescriptor<To>) =
    BindDefinition<To>(objectKey(this), objectKey(to))

class ModuleDefinition internal constructor(
    val name: String
) {

    private val definitions = mutableListOf<ProviderDefinition<*>>()

    internal fun buildInstance(context: Context): Module {
        val providers: List<ObjectProvider<*>> = definitions.map {
            it.createProvider(context)
        }
        return Module(name, providers)
    }

    inline fun <reified TypeDeclaration : Any> factory(
        noinline builder: FactoryScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return factory(TypeDeclaration::class, builder)
    }

    inline fun <reified TypeDeclaration : Any> single(
        initOnLoad: Boolean = false,
        noinline builder: SingleScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return single(TypeDeclaration::class, initOnLoad, builder)
    }

    inline fun <reified TypeDeclaration : Any> shared(
        fallbackCount: Int = 0,
        fallbackLifetime: Long = 0L,
        managerDescriptor: DependencyDescriptor<SharedScopeManager>? = null,
        noinline builder: SharedScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return shared(TypeDeclaration::class, fallbackCount, fallbackLifetime, managerDescriptor, builder)
    }

    inline fun <reified TypeDeclaration : Any> instance(
        instance: TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return instance(TypeDeclaration::class, instance)
    }

    fun <TypeDeclaration : Any> factory(
        clazz: KClass<TypeDeclaration>,
        builder: FactoryScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        if (clazz::class == Context::class) {
            throw IllegalArgumentException("Cannot register [Context] provider. Please use Descriptors.")
        }
        return addDefinition {
            factoryProvider(objectKey(clazz), BuilderMethod(builder))
        }
    }

    fun <TypeDeclaration : Any> factory(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        builder: FactoryScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            factoryProvider(objectKey(descriptor), BuilderMethod(builder))
        }
    }

    fun <TypeDeclaration : Any> shared(
        clazz: KClass<TypeDeclaration>,
        fallbackCount: Int = 0,
        fallbackLifetime: Long = 0L,
        managerDescriptor: DependencyDescriptor<SharedScopeManager>? = null,
        builder: SharedScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        if (clazz::class == Context::class) {
            throw IllegalArgumentException("Cannot register [Context] provider. Please use Descriptors.")
        }
        return addDefinition {
            sharedProvider(
                objectKey(clazz), BuilderMethod(builder), it, fallbackCount, fallbackLifetime, managerDescriptor
            )
        }
    }

    fun <TypeDeclaration : Any> shared(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        fallbackCount: Int = 0,
        fallbackLifetime: Long = 0L,
        managerDescriptor: DependencyDescriptor<SharedScopeManager>? = null,
        builder: SharedScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            sharedProvider(
                objectKey(descriptor), BuilderMethod(builder), it, fallbackCount, fallbackLifetime, managerDescriptor
            )
        }
    }

    fun <TypeDeclaration : Any> single(
        clazz: KClass<TypeDeclaration>,
        initOnLoad: Boolean = false,
        builder: SingleScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        if (clazz::class == Context::class) {
            throw IllegalArgumentException("Cannot register [Context] provider. Please use Descriptors.")
        }
        return addDefinition {
            singleProvider(objectKey(clazz), BuilderMethod(builder), initOnLoad)
        }
    }

    fun <TypeDeclaration : Any> single(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        initOnLoad: Boolean = false,
        builder: SingleScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            singleProvider(objectKey(descriptor), BuilderMethod(builder), initOnLoad)
        }
    }

    inline fun <reified To : Any> bind(
        descriptor: DependencyDescriptor<*>
    ): ProviderDefinition<To> {
        val clazz = To::class
        return bind(descriptor alsoAs clazz)
    }

    inline fun <reified To : Any> bind(
        fromClazz: KClass<*>
    ): ProviderDefinition<To> {
        val clazz = To::class
        return bind(fromClazz alsoAs clazz)
    }

    fun <To : Any> bind(
        bindDefinition: BindDefinition<To>
    ): ProviderDefinition<To> {
        return addDefinition {
            bindProvider<To>(bindDefinition.to)
        }
    }

    fun <TypeDeclaration : Any> instance(
        clazz: KClass<TypeDeclaration>,
        instance: TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        if (clazz::class == Context::class) {
            throw IllegalArgumentException("Cannot register [Context] provider. Please use Descriptors.")
        }
        return addDefinition {
            instanceProvider(objectKey(clazz), instance)
        }
    }

    fun <TypeDeclaration : Any> instance(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        instance: TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            instanceProvider(objectKey(descriptor), instance)
        }
    }

    private fun <TypeDeclaration> addDefinition(
        providerFactory: (Context) -> ObjectProvider<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        val objectDefinition = ObjectDefinition(ProviderFactoryMethod(providerFactory))
        val providerDefinition = ProviderDefinition(objectDefinition)
        definitions.add(providerDefinition)
        return providerDefinition
    }

}

internal class Module constructor(
    val name: String,
    val providers: List<ObjectProvider<*>>
) {

    fun getProvider(objectKey: ObjectKey): ObjectProvider<*>? {
        return providers.firstOrNull {
            it.isMatch(objectKey)
        }
    }

}

fun declareModule(name: String? = generatedModuleName(), initializer: ModuleDefinition.() -> Unit): ModuleDefinition {
    return ModuleDefinition(name ?: "Undefined").apply(initializer)
}

expect fun generatedModuleName(): String?

fun declareModule(builder: ModuleBuilder): ModuleDefinition {
    return ModuleDefinition(builder::class.qualifiedName ?: "Unknown").apply {
        builder.build(this)
    }
}

interface ModuleBuilder {

    fun build(definition: ModuleDefinition)

}
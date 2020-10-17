package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context
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

class ProviderDefinition<TypeDeclaration> internal constructor(
        private val objectDefinition: ObjectDefinition<TypeDeclaration>
) {

    inline fun <reified Mapping : Any> addMapping() {
        addMapping(Mapping::class)
    }

    fun <Mapping : Any> addMapping(
            clazz: KClass<Mapping>
    ) {
        objectDefinition.addMappings(objectKey(clazz))
    }

    fun <Mapping : Any> addMapping(
            descriptor: DependencyDescriptor<Mapping>
    ) {
        objectDefinition.addMappings(objectKey(descriptor))
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

    private val definitions = mutableListOf<ObjectDefinition<*>>()

    internal fun buildInstance(context: Context): Module {
        val providers: List<ObjectProvider<*>> = definitions.map {
            it.getProvider(context)
        }
        return Module(name ?: "Undefined", providers)
    }

    inline fun <reified TypeDeclaration : Any> factory(
            noinline builder: FactoryScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return factory(TypeDeclaration::class, builder)
    }

    inline fun <reified TypeDeclaration : Any> single(
            noinline builder: SingleScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return single(TypeDeclaration::class, builder)
    }

    inline fun <reified TypeDeclaration : Any> shared(
            fallbackCount: Int = 0,
            fallbackLifetime: Long = 0L,
            noinline builder: SharedScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return shared(TypeDeclaration::class, fallbackCount, fallbackLifetime, builder)
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
            builder: SharedScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        if (clazz::class == Context::class) {
            throw IllegalArgumentException("Cannot register [Context] provider. Please use Descriptors.")
        }
        return addDefinition {
            sharedDataProvider(objectKey(clazz), BuilderMethod(builder), it, fallbackCount, fallbackLifetime)
        }
    }

    fun <TypeDeclaration : Any> shared(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            fallbackCount: Int = 0,
            fallbackLifetime: Long = 0L,
            builder: SharedScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            sharedDataProvider(objectKey(descriptor), BuilderMethod(builder), it, fallbackCount, fallbackLifetime)
        }
    }

    fun <TypeDeclaration : Any> single(
            clazz: KClass<TypeDeclaration>,
            builder: SingleScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        if (clazz::class == Context::class) {
            throw IllegalArgumentException("Cannot register [Context] provider. Please use Descriptors.")
        }
        return addDefinition {
            singleProvider(objectKey(clazz), BuilderMethod(builder))
        }
    }

    fun <TypeDeclaration : Any> single(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            builder: SingleScope.() -> TypeDeclaration
    ): ProviderDefinition<TypeDeclaration> {
        return addDefinition {
            singleProvider(objectKey(descriptor), BuilderMethod(builder))
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

    private fun <TypeDeclaration> addDefinition(
            providerFactory: (Context) -> ObjectProvider<TypeDeclaration>
    ): ProviderDefinition<TypeDeclaration> {
        val definition = ObjectDefinition(ProviderFactoryMethod(providerFactory))
        definitions.add(definition)
        return ProviderDefinition(definition)
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
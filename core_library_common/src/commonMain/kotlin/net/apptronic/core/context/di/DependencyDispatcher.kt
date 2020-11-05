package net.apptronic.core.context.di

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import kotlin.reflect.KClass

fun Context.dependencyDispatcher(): DependencyDispatcher {
    return LazyDependencyDispatcher(this)
}

/**
 * Class for providing dependencies
 */
abstract class DependencyDispatcher : DependencyProvider {

    abstract val context: Context

    val parent: DependencyDispatcher?
        get() {
            return context.parent?.dependencyDispatcher
        }

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyDispatcher]
     * @param name optional name for instance
     */
    inline fun <reified TypeDeclaration : Any> addInstance(
            instance: TypeDeclaration
    ) {
        addInstance(TypeDeclaration::class, instance)
    }

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyDispatcher]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    abstract fun <TypeDeclaration : Any> addInstance(
            clazz: KClass<TypeDeclaration>,
            instance: TypeDeclaration
    )

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyDispatcher]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    abstract fun <TypeDeclaration : Any> addInstance(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            instance: TypeDeclaration
    )

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyDispatcher]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    abstract fun <TypeDeclaration : Any> addNullableInstance(
            descriptor: DependencyDescriptor<TypeDeclaration?>,
            instance: TypeDeclaration?
    )

    abstract fun addModule(moduleDefinition: ModuleDefinition)

    final override fun <TypeDeclaration : Any> inject(
            clazz: KClass<TypeDeclaration>,
            params: Parameters
    ): TypeDeclaration {
        val key = objectKey(clazz)
        return getInstanceInternal(key, params)
    }

    final override fun <TypeDeclaration : Any> optional(
            clazz: KClass<TypeDeclaration>,
            params: Parameters
    ): TypeDeclaration? {
        val key = objectKey(clazz)
        return getOptionalInternal(key, params)
    }

    final override fun <TypeDeclaration : Any> injectLazy(
            clazz: KClass<TypeDeclaration>,
            params: Parameters
    ): Lazy<TypeDeclaration> {
        val key = objectKey(clazz)
        return getLazyInstanceInternal(key, params)
    }

    final override fun <TypeDeclaration : Any> optionalLazy(
            clazz: KClass<TypeDeclaration>,
            params: Parameters
    ): Lazy<TypeDeclaration?> {
        val key = objectKey(clazz)
        return getLazyOptionalInternal(key, params)
    }

    final override fun <TypeDeclaration> inject(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters
    ): TypeDeclaration {
        val key = objectKey(descriptor)
        return getInstanceInternal(key, params)
    }

    final override fun <TypeDeclaration> optional(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters
    ): TypeDeclaration? {
        val key = objectKey(descriptor)
        return getOptionalInternal(key, params)
    }

    final override fun <TypeDeclaration> injectLazy(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters
    ): Lazy<TypeDeclaration> {
        val key = objectKey(descriptor)
        return getLazyInstanceInternal(key, params)
    }

    final override fun <TypeDeclaration> optionalLazy(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters
    ): Lazy<TypeDeclaration?> {
        val key = objectKey(descriptor)
        return getLazyOptionalInternal(key, params)
    }

    internal fun <TypeDeclaration> inject(
            objectKey: ObjectKey,
            params: Parameters
    ): TypeDeclaration {
        return getInstanceInternal(objectKey, params)
    }

    internal fun <TypeDeclaration> optional(
            objectKey: ObjectKey,
            params: Parameters
    ): TypeDeclaration? {
        return getOptionalInternal(objectKey, params)
    }

    internal fun <TypeDeclaration> injectLazy(
            objectKey: ObjectKey,
            params: Parameters
    ): Lazy<TypeDeclaration> {
        return getLazyInstanceInternal(objectKey, params)
    }

    internal fun <TypeDeclaration> optionalLazy(
            objectKey: ObjectKey,
            params: Parameters
    ): Lazy<TypeDeclaration?> {
        return getLazyOptionalInternal(objectKey, params)
    }

    private fun <TypeDeclaration> getLazyInstanceInternal(
            key: ObjectKey,
            params: Parameters
    ): Lazy<TypeDeclaration> {
        return lazy {
            getInstanceInternal<TypeDeclaration>(key, params)
        }
    }

    private fun <TypeDeclaration> getLazyOptionalInternal(
            key: ObjectKey,
            params: Parameters
    ): Lazy<TypeDeclaration?> {
        return lazy {
            try {
                getInstanceInternal<TypeDeclaration>(key, params)
            } catch (e: InjectionFailedException) {
                null
            }
        }
    }

    internal abstract fun <TypeDeclaration> searchInstance(searchSpec: SearchSpec): ValueHolder<TypeDeclaration>?

    private fun <TypeDeclaration> getInstanceInternal(
            key: ObjectKey,
            params: Parameters
    ): TypeDeclaration {
        return performInject<TypeDeclaration>(SearchSpec(context, key, params)).value
    }

    private fun <TypeDeclaration> getOptionalInternal(
            key: ObjectKey,
            params: Parameters
    ): TypeDeclaration? {
        return try {
            performInject<TypeDeclaration>(SearchSpec(context, key, params)).value
        } catch (e: InjectionFailedException) {
            null
        }
    }

    private fun <TypeDeclaration> performInject(searchSpec: SearchSpec): ValueHolder<TypeDeclaration> {
        val result: ValueHolder<TypeDeclaration>? = searchInstance(searchSpec)
        return result
                ?: throw InjectionFailedException("Object ${searchSpec.key} is not found:\n${searchSpec.getSearchPath()}")
    }

    abstract fun traceDependencyTree(): DependencyTrace

    internal abstract fun getInstanceNames(): List<String>

    internal abstract fun getModuleNames(): List<String>

    internal abstract fun traceCurrent(): DependencyTraceElement

}
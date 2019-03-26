package net.apptronic.core.component.di

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.lifecycle.LifecycleStage
import kotlin.reflect.KClass

/**
 * Class for providing dependencies
 */
class DependencyProvider(
    private val context: ComponentContext,
    private val parent: DependencyProvider?
) {

    private val externalInstances = mutableMapOf<ObjectKey, Any>()
    private val modules = mutableListOf<Module>()

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyProvider]
     * @param name optional name for instance
     */
    inline fun <reified TypeDeclaration : Any> addInstance(
        instance: TypeDeclaration,
        descriptor: Descriptor<TypeDeclaration>? = null
    ) {
        addInstance(instance, TypeDeclaration::class, descriptor)
    }

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyProvider]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    fun <TypeDeclaration : Any> addInstance(
        instance: TypeDeclaration, clazz:
        KClass<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null
    ) {
        val key = objectKey(clazz, descriptor)
        externalInstances[key] = instance
    }

    fun addModule(moduleDefinition: ModuleDefinition) {
        modules.add(moduleDefinition.buildInstance(this))
    }

    inline fun <reified TypeDeclaration : Any> get(
        descriptor: Descriptor<TypeDeclaration>? = null,
        params: Parameters = parameters { }
    ): TypeDeclaration {
        return get(TypeDeclaration::class, descriptor, params)
    }

    inline fun <reified TypeDeclaration : Any> lazy(
        descriptor: Descriptor<TypeDeclaration>? = null,
        params: Parameters = parameters { }
    ): Lazy<TypeDeclaration> {
        return lazy(TypeDeclaration::class, descriptor, params)
    }

    fun <TypeDeclaration> get(
        clazz: Class<*>
    ): TypeDeclaration {
        val key = objectKey(clazz, null)
        return getInstanceInternal(key, emptyParameters())
    }

    fun <TypeDeclaration> get(
        clazz: Class<*>,
        descriptor: Descriptor<TypeDeclaration>? = null
    ): TypeDeclaration {
        val key = objectKey(clazz, descriptor)
        return getInstanceInternal(key, emptyParameters())
    }

    fun <TypeDeclaration> lazy(
        clazz: Class<*>,
        descriptor: Descriptor<TypeDeclaration>? = null
    ): Lazy<TypeDeclaration> {
        val key = objectKey(clazz, descriptor)
        return getLazyInstanceInternal(key, emptyParameters())
    }

    fun <TypeDeclaration : Any> get(
        clazz: KClass<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null
    ): TypeDeclaration {
        val key = objectKey(clazz, descriptor)
        return getInstanceInternal(key, emptyParameters())
    }

    fun <TypeDeclaration : Any> lazy(
        clazz: KClass<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null
    ): Lazy<TypeDeclaration> {
        val key = objectKey(clazz, descriptor)
        return getLazyInstanceInternal(key, emptyParameters())
    }

    fun <TypeDeclaration : Any> get(
        clazz: KClass<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null,
        params: Parameters
    ): TypeDeclaration {
        val key = objectKey(clazz, descriptor)
        return getInstanceInternal(key, params)
    }

    fun <TypeDeclaration : Any> lazy(
        clazz: KClass<TypeDeclaration>,
        descriptor: Descriptor<TypeDeclaration>? = null,
        params: Parameters
    ): Lazy<TypeDeclaration> {
        val key = objectKey(clazz, descriptor)
        return getLazyInstanceInternal(key, params)
    }

    internal fun <TypeDeclaration> get(
        objectKey: ObjectKey,
        params: Parameters = parameters { }
    ): TypeDeclaration {
        return getInstanceInternal(objectKey, params)
    }

    private fun <TypeDeclaration> getInstanceInternal(
        key: ObjectKey,
        params: Parameters
    ): TypeDeclaration {
        val localLifecycleStage = context.getLifecycle().getActiveStage()
            ?: throw IllegalStateException("Lifecycle was terminated")
        return searchInstance(localLifecycleStage, key, params)
    }

    private fun <TypeDeclaration> getLazyInstanceInternal(
        key: ObjectKey,
        params: Parameters
    ): Lazy<TypeDeclaration> {
        val localLifecycleStage = context.getLifecycle().getActiveStage()
            ?: throw IllegalStateException("Lifecycle was terminated")
        return lazy {
            searchInstance<TypeDeclaration>(localLifecycleStage, key, params)
        }
    }

    private fun <TypeDeclaration> searchInstance(
        callerLifecycleStage: LifecycleStage,
        key: ObjectKey,
        params: Parameters
    ): TypeDeclaration {
        val localLifecycleStage = context.getLifecycle().getActiveStage()
            ?: throw IllegalStateException("Lifecycle was terminated")
        val local: TypeDeclaration? =
            obtainLocalInstance(callerLifecycleStage, localLifecycleStage, key, params)
        if (local != null) {
            return local
        }
        val parental: TypeDeclaration? = parent?.searchInstance(callerLifecycleStage, key, params)
        if (parental != null) {
            return parental
        }
        throw ObjectNotFoundException("Object $key or it's factory/cast is not found in current context and all parental contexts")
    }

    private fun <TypeDeclaration> obtainLocalInstance(
        callerLifecycleStage: LifecycleStage,
        localLifecycleStage: LifecycleStage,
        objectKey: ObjectKey,
        params: Parameters
    ): TypeDeclaration? {
        val obj = externalInstances[objectKey]
        if (obj != null) {
            return obj as TypeDeclaration
        }
        modules.forEach { module ->
            val providerSearch = module.getProvider(objectKey)
            if (providerSearch != null) {
                val provider = providerSearch as ObjectProvider<TypeDeclaration>
                val factoryContext = FactoryContext(
                    this,
                    params,
                    localLifecycleStage,
                    callerLifecycleStage
                )
                return@obtainLocalInstance provider.provide(factoryContext)
            }
        }
        return null
    }

}
package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.lifecycle.LifecycleStage
import kotlin.reflect.KClass

/**
 * Class for providing dependencies
 */
class DependencyProvider(
    private val context: Context,
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
        instance: TypeDeclaration
    ) {
        addInstance(TypeDeclaration::class, instance)
    }

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyProvider]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    fun <TypeDeclaration : Any> addInstance(
        clazz: KClass<TypeDeclaration>,
        instance: TypeDeclaration
    ) {
        val key = objectKey(clazz)
        externalInstances[key] = instance
    }

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyProvider]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    fun <TypeDeclaration : Any> addInstance(
        descriptor: Descriptor<TypeDeclaration>,
        instance: TypeDeclaration
    ) {
        val key = objectKey(descriptor)
        externalInstances[key] = instance
    }

    fun addModule(moduleDefinition: ModuleDefinition) {
        modules.add(moduleDefinition.buildInstance(this))
    }

    inline fun <reified TypeDeclaration : Any> inject(
        params: Parameters = emptyParameters()
    ): TypeDeclaration {
        return inject(TypeDeclaration::class, params)
    }

    inline fun <reified TypeDeclaration : Any> injectLazy(
        params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration> {
        return injectLazy(TypeDeclaration::class, params)
    }

    fun <TypeDeclaration : Any> inject(
        clazz: KClass<TypeDeclaration>,
        params: Parameters = emptyParameters()
    ): TypeDeclaration {
        val key = objectKey(clazz)
        return getInstanceInternal(key, params)
    }

    fun <TypeDeclaration : Any> injectLazy(
        clazz: KClass<TypeDeclaration>,
        params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration> {
        val key = objectKey(clazz)
        return getLazyInstanceInternal(key, params)
    }

    fun <TypeDeclaration> inject(
        descriptor: Descriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
    ): TypeDeclaration {
        val key = objectKey(descriptor)
        return getInstanceInternal(key, params)
    }

    fun <TypeDeclaration> injectLazy(
        descriptor: Descriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration> {
        val key = objectKey(descriptor)
        return getLazyInstanceInternal(key, params)
    }

    internal fun <TypeDeclaration> inject(
        objectKey: ObjectKey,
        params: Parameters = parameters { }
    ): TypeDeclaration {
        return getInstanceInternal(objectKey, params)
    }

    internal fun <TypeDeclaration> injectLazy(
        objectKey: ObjectKey,
        params: Parameters = parameters { }
    ): Lazy<TypeDeclaration> {
        return getLazyInstanceInternal(objectKey, params)
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
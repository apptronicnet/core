package net.apptronic.core.component.di

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import kotlin.reflect.KClass

/**
 * Class for providing dependencies
 */
class DependencyProvider(
    private val context: Context,
    private val parent: DependencyProvider?
) {

    private val externalInstances = mutableMapOf<ObjectKey, ValueHolder<*>>()
    private val modules = mutableListOf<Module>()
    private val logger = context.getLogger()

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
        externalInstances[key] = ValueHolder(instance)
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
        externalInstances[key] = ValueHolder(instance)
    }

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyProvider]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    fun <TypeDeclaration : Any> addNullableInstance(
        descriptor: Descriptor<TypeDeclaration?>,
        instance: TypeDeclaration?
    ) {
        val key = objectKey(descriptor)
        externalInstances[key] = ValueHolder(instance)
    }

    fun addModule(moduleDefinition: ModuleDefinition) {
        modules.add(moduleDefinition.buildInstance())
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
        return performInject<TypeDeclaration>(SearchSpec(context, key, params)).value
    }

    private fun <TypeDeclaration> getLazyInstanceInternal(
        key: ObjectKey,
        params: Parameters
    ): Lazy<TypeDeclaration> {
        return lazy {
            getInstanceInternal<TypeDeclaration>(key, params)
        }
    }

    internal fun getInstanceNames(): List<String> {
        return externalInstances.entries.map {
            "${it.key} = ${it.value}}"
        }
    }

    internal fun getModuleNames(): List<String> {
        return modules.map { it.name }
    }

    private fun <TypeDeclaration> performInject(searchSpec: SearchSpec): ValueHolder<TypeDeclaration> {
        var result: ValueHolder<TypeDeclaration>? = null
        val time = measureNanoTime {
            result = searchInstance(searchSpec)
        }
        val timeFormatted = "%.6f".format(time.toFloat() / 1000000F)
        logger.log("Injected ${searchSpec.key} using ${searchSpec.context} in $timeFormatted ms")
        return result
            ?: throw InjectionFailedException("Object ${searchSpec.key} is not found:\n${searchSpec.getSearchPath()}")
    }

    private fun <TypeDeclaration> searchInstance(
        searchSpec: SearchSpec
    ): ValueHolder<TypeDeclaration>? {
        searchSpec.addContextToChain(context)
        val local: ValueHolder<TypeDeclaration>? = obtainLocalInstance(searchSpec)
        if (local != null) {
            return local
        }
        val parental: ValueHolder<TypeDeclaration>? = parent?.searchInstance(searchSpec)
        if (parental != null) {
            return parental
        }
        return null
    }

    private fun <TypeDeclaration> obtainLocalInstance(
        searchSpec: SearchSpec
    ): ValueHolder<TypeDeclaration>? {
        val obj = externalInstances[searchSpec.key]
        if (obj != null) {
            return obj as ValueHolder<TypeDeclaration>
        }
        modules.forEach { module ->
            val providerSearch = module.getProvider(searchSpec.key)
            if (providerSearch != null) {
                val provider = providerSearch as ObjectProvider<TypeDeclaration>
                val instance = provider.provide(context, this, searchSpec)
                return@obtainLocalInstance ValueHolder(instance)
            }
        }
        return null
    }

}
package net.apptronic.core.component.di

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.util.*
import kotlin.reflect.KClass

/**
 * Class for providing dependencies
 */
class DependencyDispatcher(
        internal val context: Context,
        private val parent: DependencyDispatcher?
) : DependencyProvider {

    private val externalInstances = mutableMapOf<ObjectKey, ValueHolder<*>>()
    private val modules = mutableListOf<Module>()

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
    fun <TypeDeclaration : Any> addInstance(
            clazz: KClass<TypeDeclaration>,
            instance: TypeDeclaration
    ) {
        if (instance is ModuleDefinition) {
            throw IllegalArgumentException("ModuleDefinition should be added using addModule()")
        }
        val key = objectKey(clazz)
        externalInstances[key] = ValueHolder(instance)
        context.lifecycle.onExitFromActiveStage {
            externalInstances.remove(key)
        }
    }

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyDispatcher]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    fun <TypeDeclaration : Any> addInstance(
            descriptor: Descriptor<TypeDeclaration>,
            instance: TypeDeclaration
    ) {
        if (instance is ModuleDefinition) {
            throw IllegalArgumentException("ModuleDefinition should be added using addModule()")
        }
        val key = objectKey(descriptor)
        externalInstances[key] = ValueHolder(instance)
    }

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [DependencyDispatcher]
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

    override fun <TypeDeclaration : Any> inject(
            clazz: KClass<TypeDeclaration>,
            params: Parameters
    ): TypeDeclaration {
        val key = objectKey(clazz)
        return getInstanceInternal(key, params)
    }

    override fun <TypeDeclaration : Any> optional(
            clazz: KClass<TypeDeclaration>,
            params: Parameters
    ): TypeDeclaration? {
        val key = objectKey(clazz)
        return getOptionalInternal(key, params)
    }

    override fun <TypeDeclaration : Any> injectLazy(
            clazz: KClass<TypeDeclaration>,
            params: Parameters
    ): Lazy<TypeDeclaration> {
        val key = objectKey(clazz)
        return getLazyInstanceInternal(key, params)
    }

    override fun <TypeDeclaration : Any> optionalLazy(
            clazz: KClass<TypeDeclaration>,
            params: Parameters
    ): Lazy<TypeDeclaration?> {
        val key = objectKey(clazz)
        return getLazyOptionalInternal(key, params)
    }

    override fun <TypeDeclaration> inject(
            descriptor: Descriptor<TypeDeclaration>,
            params: Parameters
    ): TypeDeclaration {
        val key = objectKey(descriptor)
        return getInstanceInternal(key, params)
    }

    override fun <TypeDeclaration> optional(
            descriptor: Descriptor<TypeDeclaration>,
            params: Parameters
    ): TypeDeclaration? {
        val key = objectKey(descriptor)
        return getOptionalInternal(key, params)
    }

    override fun <TypeDeclaration> injectLazy(
            descriptor: Descriptor<TypeDeclaration>,
            params: Parameters
    ): Lazy<TypeDeclaration> {
        val key = objectKey(descriptor)
        return getLazyInstanceInternal(key, params)
    }

    override fun <TypeDeclaration> optionalLazy(
            descriptor: Descriptor<TypeDeclaration>,
            params: Parameters
    ): Lazy<TypeDeclaration?> {
        val key = objectKey(descriptor)
        return getLazyOptionalInternal(key, params)
    }

    internal fun <TypeDeclaration> inject(
            objectKey: ObjectKey,
            params: Parameters = parameters { }
    ): TypeDeclaration {
        return getInstanceInternal(objectKey, params)
    }

    internal fun <TypeDeclaration> optional(
            objectKey: ObjectKey,
            params: Parameters = parameters { }
    ): TypeDeclaration? {
        return getOptionalInternal(objectKey, params)
    }

    internal fun <TypeDeclaration> injectLazy(
            objectKey: ObjectKey,
            params: Parameters = parameters { }
    ): Lazy<TypeDeclaration> {
        return getLazyInstanceInternal(objectKey, params)
    }

    internal fun <TypeDeclaration> optionalLazy(
            objectKey: ObjectKey,
            params: Parameters = parameters { }
    ): Lazy<TypeDeclaration?> {
        return getLazyOptionalInternal(objectKey, params)
    }

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

    internal fun getInstanceNames(): List<String> {
        return externalInstances.entries.map {
            "${it.key} = ${it.value}}"
        }
    }

    internal fun getModuleNames(): List<String> {
        return modules.map { it.name }
    }

    private fun <TypeDeclaration> performInject(searchSpec: SearchSpec): ValueHolder<TypeDeclaration> {
        var result: ValueHolder<TypeDeclaration>? = searchInstance(searchSpec)
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

    fun traceDependencyTree(): DependencyTrace {
        val traces = mutableListOf<DependencyTraceElement>()
        var dispatcher: DependencyDispatcher? = this
        while (dispatcher != null) {
            traces.add(dispatcher.traceCurrent())
            dispatcher = dispatcher.parent
        }
        return DependencyTrace(traces)
    }

    private fun traceCurrent(): DependencyTraceElement {
        val name = context.toString()
        val instanceTraces = externalInstances.entries.map {
            DependencyTraceInstance(
                    it.key.toString(),
                    it.value
            )
        }
        val moduleTraces = this.modules.map { module ->
            DependencyTraceModule(
                    name = module.name,
                    providers = module.providers.map { provider ->
                        DependencyTraceProvider(
                                type = provider.typeName,
                                keys = provider.getMappings().map { objectKey ->
                                    objectKey.toString()
                                }
                        )
                    }
            )
        }
        return DependencyTraceElement(name, instanceTraces, moduleTraces)
    }

}
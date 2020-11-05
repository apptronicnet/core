package net.apptronic.core.context.di

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import kotlin.reflect.KClass

/**
 * Class for providing dependencies
 */
internal class ContextDependencyDispatcher(
        override val context: Context
) : DependencyDispatcher() {

    private val externalInstances = mutableMapOf<ObjectKey, ValueHolder<*>>()
    private val modules = mutableListOf<Module>()

    /**
     * Add external instance to this provider.
     * @param instance instance which can be injected using this [ContextDependencyDispatcher]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    override fun <TypeDeclaration : Any> addInstance(
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
     * @param instance instance which can be injected using this [ContextDependencyDispatcher]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    override fun <TypeDeclaration : Any> addInstance(
            descriptor: DependencyDescriptor<TypeDeclaration>,
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
     * @param instance instance which can be injected using this [ContextDependencyDispatcher]
     * @param clazz optional class for this instance
     * @param name optional name for instance
     */
    override fun <TypeDeclaration : Any> addNullableInstance(
            descriptor: DependencyDescriptor<TypeDeclaration?>,
            instance: TypeDeclaration?
    ) {
        val key = objectKey(descriptor)
        externalInstances[key] = ValueHolder(instance)
    }

    override fun addModule(moduleDefinition: ModuleDefinition) {
        modules.add(moduleDefinition.buildInstance(context))
    }

    override fun getInstanceNames(): List<String> {
        return externalInstances.entries.map {
            "${it.key} = ${it.value}}"
        }
    }

    override fun getModuleNames(): List<String> {
        return modules.map { it.name }
    }

    override fun <TypeDeclaration> searchInstance(
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

    @Suppress("UNCHECKED_CAST")
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
                val processed = context.plugins.nextInject(context, searchSpec.context, instance)
                return@obtainLocalInstance ValueHolder(processed)
            }
        }
        return null
    }

    override fun traceDependencyTree(): DependencyTrace {
        val traces = mutableListOf<DependencyTraceElement>()
        var dispatcher: DependencyDispatcher? = this
        while (dispatcher != null) {
            traces.add(dispatcher.traceCurrent())
            dispatcher = dispatcher.parent
        }
        return DependencyTrace(traces)
    }

    override fun traceCurrent(): DependencyTraceElement {
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
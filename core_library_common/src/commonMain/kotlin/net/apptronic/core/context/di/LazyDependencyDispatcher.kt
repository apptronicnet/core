package net.apptronic.core.context.di

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import kotlin.reflect.KClass

/**
 * Simulates implementation of [DependencyDispatcher], but creates real [ContextDependencyDispatcher] only when
 * something added. Until nothing is added (modules or instances) it will resend all request to [parent] or return
 * empty stubs.
 */
internal class LazyDependencyDispatcher(
        override val context: Context
) : DependencyDispatcher() {

    /**
     * Optional "this" instance. Not exists until module or instance added.
     */
    private var optionalnstance: DependencyDispatcher? = null

    /**
     * Return "this" instance if exists. Will create "this" instance if it is not exists.
     */
    private val requireInstance: DependencyDispatcher
        get() {
            val value = this.optionalnstance
            return if (value != null) {
                value
            } else {
                val created = ContextDependencyDispatcher(context)
                this.optionalnstance = created
                created
            }
        }

    /**
     * Return "this" instance if exists or is there is no parent.
     * In case when "this" instance is not exists and parent exists - will return parent.
     */
    private val thisOrParentInstance: DependencyDispatcher
        get() {
            return optionalnstance ?: parent ?: requireInstance
        }

    override fun <TypeDeclaration : Any> addInstance(clazz: KClass<TypeDeclaration>, instance: TypeDeclaration) {
        return requireInstance.addInstance(clazz, instance)
    }

    override fun <TypeDeclaration : Any> addInstance(descriptor: DependencyDescriptor<TypeDeclaration>, instance: TypeDeclaration) {
        return requireInstance.addInstance(descriptor, instance)
    }

    override fun <TypeDeclaration : Any> addNullableInstance(descriptor: DependencyDescriptor<TypeDeclaration?>, instance: TypeDeclaration?) {
        return requireInstance.addNullableInstance(descriptor, instance)
    }

    override fun addModule(moduleDefinition: ModuleDefinition) {
        return requireInstance.addModule(moduleDefinition)
    }

    override fun <TypeDeclaration> searchInstance(searchSpec: SearchSpec): ValueHolder<TypeDeclaration>? {
        return thisOrParentInstance.searchInstance(searchSpec)
    }

    override fun getInstanceNames(): List<String> {
        return optionalnstance?.getInstanceNames() ?: emptyList()
    }

    override fun getModuleNames(): List<String> {
        return optionalnstance?.getModuleNames() ?: emptyList()
    }

    override fun traceDependencyTree(): DependencyTrace {
        return optionalnstance?.traceDependencyTree() ?: DependencyTrace(emptyList())
    }

    override fun traceCurrent(): DependencyTraceElement {
        return optionalnstance?.traceCurrent() ?: run {
            val name = context.toString()
            DependencyTraceElement(name, emptyList(), emptyList())
        }
    }

}
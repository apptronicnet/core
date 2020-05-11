package net.apptronic.core.component.context

import net.apptronic.core.component.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.component.lifecycle.LifecycleDefinition

fun defineContext(
        lifecycleDefinition: LifecycleDefinition = BASE_LIFECYCLE,
        contextBuilder: Context.() -> Unit
): ContextDefinition<Context> {
    return BasicContextDefinition(contextBuilder, lifecycleDefinition)
}

fun <ContextType : Context> defineTypedContext(builder: (Context) -> ContextType): ContextDefinition<ContextType> {
    return TypedBuilderContextDefinition(builder)
}

val EMPTY_CONTEXT = defineContext {
    // nothing to add
}

interface ContextDefinition<ContextType : Context> {

    fun createContext(parent: Context): ContextType

}

fun <ContextType : Context> ContextDefinition<ContextType>.with(
        instanceInitializer: ContextType.() -> Unit
): ContextDefinition<ContextType> {
    return WithInitializerContextDefinition(this, instanceInitializer)
}

private class BasicContextDefinition(
        private val contextBuilder: Context.() -> Unit,
        private val lifecycleDefinition: LifecycleDefinition
) : ContextDefinition<Context> {

    override fun createContext(parent: Context): Context {
        val context = SubContext(parent, lifecycleDefinition)
        context.contextBuilder()
        return context
    }

}

private class WithInitializerContextDefinition<ContextType : Context>(
        private val targetDefinition: ContextDefinition<ContextType>,
        private val instanceInitializer: ContextType.() -> Unit
) : ContextDefinition<ContextType> {

    override fun createContext(parent: Context): ContextType {
        val context = targetDefinition.createContext(parent)
        context.instanceInitializer()
        return context
    }

}

private class TypedBuilderContextDefinition<ContextType : Context>(
        private val builder: (Context) -> ContextType
) : ContextDefinition<ContextType> {

    override fun createContext(parent: Context): ContextType {
        return builder(parent)
    }

}
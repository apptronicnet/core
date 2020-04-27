package net.apptronic.core.component.extensions

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun defineContext(contextBuilder: Context.() -> Unit): ContextDefinition<Context> {
    return BasicContextDefinition(contextBuilder)
}

fun defineViewModelContext(
        name: String, contextBuilder: Context.() -> Unit
): ContextDefinition<ViewModelContext> {
    return ViewModelContextDefinition(name, contextBuilder)
}

interface ContextDefinition<ContextType : Context> {

    fun createContext(parent: Context): ContextType

}

internal class BasicContextDefinition(
        private val contextBuilder: Context.() -> Unit
) : ContextDefinition<Context> {

    override fun createContext(parent: Context): Context {
        return buildContext(parent, Lifecycle(), block = contextBuilder)
    }

}

internal class ViewModelContextDefinition(
        private val name: String,
        private val contextBuilder: ViewModelContext.() -> Unit
) : ContextDefinition<ViewModelContext> {

    override fun createContext(parent: Context): ViewModelContext {
        val context = ViewModelContext(parent, name)
        contextBuilder.invoke(context)
        return context
    }

}
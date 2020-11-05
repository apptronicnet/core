package net.apptronic.core.viewmodel

import net.apptronic.core.context.Context
import net.apptronic.core.context.ContextDefinition

val EmptyViewModelContext = defineViewModelContext(name = "empty")

fun defineViewModelContext(
        name: String = "unnamed",
        contextBuilder: ViewModelContext.() -> Unit = {}
): ContextDefinition<ViewModelContext> {
    return ViewModelContextDefinition(name, contextBuilder)
}

private class ViewModelContextDefinition(
        private val name: String,
        private val contextBuilder: ViewModelContext.() -> Unit
) : ContextDefinition<ViewModelContext> {

    override fun createContext(parent: Context): ViewModelContext {
        return ViewModelContext(parent, name).also {
            contextBuilder(it)
        }
    }

}
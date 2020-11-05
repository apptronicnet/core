package net.apptronic.core.viewmodel

import net.apptronic.core.context.Context
import net.apptronic.core.context.ContextDefinition
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.SubContext


fun Contextual.viewModelContext(definition: ContextDefinition<ViewModelContext>): ViewModelContext {
    return definition.createContext(context)
}

fun Contextual.viewModelContext(name: String = "ViewModelContext", builder: ViewModelContext.() -> Unit = {}): ViewModelContext {
    val viewModelContext = ViewModelContext(parent = context, name = name)
    viewModelContext.builder()
    return viewModelContext
}

class ViewModelContext internal constructor(
        parent: Context, private val name: String
) : SubContext(parent, VIEW_MODEL_LIFECYCLE) {

    override val context: ViewModelContext
        get() = this

    override fun toString(): String {
        return "ViewModelContext/$name"
    }

}
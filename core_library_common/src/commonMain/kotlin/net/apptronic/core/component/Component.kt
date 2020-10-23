package net.apptronic.core.component

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition

open class Component : AbstractComponent {

    final override val context: Context

    constructor(context: Context) : super() {
        this.context = context
        applyPlugins()
    }

    constructor(parentContext: Context, contextDefinition: ContextDefinition<Context>) : super() {
        context = contextDefinition.createContext(parentContext)
        applyPlugins()
    }

}
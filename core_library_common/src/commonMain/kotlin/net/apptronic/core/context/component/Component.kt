package net.apptronic.core.context.component

import net.apptronic.core.context.Context
import net.apptronic.core.context.ContextDefinition

open class Component : AbstractComponent {

    final override val context: Context

    constructor(context: Context) : super() {
        this.context = context
        doInit()
    }

    constructor(parentContext: Context, contextDefinition: ContextDefinition<Context>) : super() {
        context = contextDefinition.createContext(parentContext)
        doInit()
    }

    private fun doInit() {
        context.addComponent(this)
    }

}
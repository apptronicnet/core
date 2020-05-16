package net.apptronic.core.component.extensions

import net.apptronic.core.component.Component
import net.apptronic.core.component.applyPlugins
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition

open class BaseComponent : Component {

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
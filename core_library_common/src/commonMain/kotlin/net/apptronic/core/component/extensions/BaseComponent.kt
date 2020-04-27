package net.apptronic.core.component.extensions

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context

open class BaseComponent : Component {

    override val context: Context

    constructor(context: Context) {
        this.context = context
    }

    constructor(
            parentContext: Context,
            contextDefinition: ContextDefinition<Context>
    ) {
        this.context = contextDefinition.createContext(parentContext)
    }

}
package net.apptronic.core.component.plugin

import net.apptronic.core.component.Component
import net.apptronic.core.component.IComponent
import net.apptronic.core.component.context.Context

/**
 * Base class which can extend functionality for [Context] and [Component]. Plugin applies to target [Context]
 * and all children if it
 */
abstract class Plugin {

    open fun onInstall(context: Context) {

    }

    open fun onContext(context: Context) {

    }

    open fun onComponent(component: IComponent) {

    }

}
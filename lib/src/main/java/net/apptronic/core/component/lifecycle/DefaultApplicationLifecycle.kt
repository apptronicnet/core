package net.apptronic.core.component.lifecycle

import net.apptronic.core.component.context.ComponentContext

class DefaultApplicationLifecycle(context: ComponentContext) : Lifecycle(context.workers()) {

}
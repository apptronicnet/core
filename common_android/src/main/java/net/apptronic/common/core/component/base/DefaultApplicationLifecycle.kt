package net.apptronic.common.core.component.base

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.lifecycle.Lifecycle

class DefaultApplicationLifecycle(context: ComponentContext) : Lifecycle(context.workers()) {

}
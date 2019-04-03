package net.apptronic.core.component.lifecycle

import net.apptronic.core.component.context.Context

class DefaultApplicationLifecycle(context: Context) : Lifecycle(context.getWorkers()) {

}
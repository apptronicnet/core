package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.UpdatePredicate

abstract class ComponentEvent<T>(
    context: Context
) : ComponentEntity<T>(context, UpdatePredicate()) {

    fun sendEvent(event: T) {
        workingPredicate.update(event)
    }

}

class ComponentGenericEvent(context: Context) : ComponentEvent<Unit>(context) {

    fun sendEvent() {
        sendEvent(Unit)
    }

}

class ComponentTypedEvent<T>(context: Context) : ComponentEvent<T>(context) {

    fun send(event: T) {
        sendEvent(event)
    }

}
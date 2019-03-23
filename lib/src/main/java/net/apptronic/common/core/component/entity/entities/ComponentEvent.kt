package net.apptronic.common.core.component.entity.entities

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.entity.base.UpdatePredicate

abstract class ComponentEvent<T>(
    context: ComponentContext
) : ComponentEntity<T>(context, UpdatePredicate()) {

    fun sendEvent(event: T) {
        workingPredicate.update(event)
    }

}

class ComponentGenericEvent(context: ComponentContext) : ComponentEvent<Unit>(context) {

    fun sendEvent() {
        sendEvent(Unit)
    }

}

class ComponentTypedEvent<T>(context: ComponentContext) : ComponentEvent<T>(context) {

    fun send(event: T) {
        sendEvent(event)
    }

}
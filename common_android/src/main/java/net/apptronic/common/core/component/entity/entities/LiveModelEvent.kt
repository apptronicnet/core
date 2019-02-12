package net.apptronic.common.core.component.entity.entities

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.entity.base.UpdatePredicate

abstract class LiveModelEvent<T>(
    context: ComponentContext
) : LiveModelEntity<T>(context, UpdatePredicate()) {

    fun sendEvent(event: T) {
        workingPredicate.update(event)
    }

}

class LiveModelGenericEvent(context: ComponentContext) : LiveModelEvent<Unit>(context) {

    fun sendEvent() {
        sendEvent(Unit)
    }

}
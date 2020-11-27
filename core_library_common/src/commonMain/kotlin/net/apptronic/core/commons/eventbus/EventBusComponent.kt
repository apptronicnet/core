package net.apptronic.core.commons.eventbus

import kotlinx.coroutines.launch
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope

@UnderDevelopment
internal class EventBusComponent(context: Context) : Component(context) {

    private val scope = contextCoroutineScope

    private class Reference(val observer: EventObserver<*>)

    private val references = mutableListOf<Reference>()

    fun postEvent(eventTransportObject: EventTransportObject<*>) {
        scope.launch {
            for (ref in references) {
                ref.observer.processEvent(eventTransportObject)
            }
        }
    }

    fun observeEvents(targetContext: Context, observer: EventObserver<*>) {
        if (targetContext.lifecycle.isTerminated()) {
            return
        }
        val reference = Reference(observer)
        references.add(reference)
        targetContext.lifecycle.onExitFromActiveStage {
            references.remove(reference)
        }
    }

}
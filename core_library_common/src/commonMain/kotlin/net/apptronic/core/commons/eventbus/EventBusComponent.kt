package net.apptronic.core.commons.eventbus

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope

internal class EventBusComponent(context: Context) : Component(context) {

    private val scope: CoroutineScope = contextCoroutineScope

    private class Reference(val observer: EventObserver<*>)

    private val references = mutableListOf<Reference>()

    fun postEvent(eventTransportObject: EventTransportObject<*>) {
        // prevent concurrent modification
        val iterator = mutableListOf<Reference>().apply {
            addAll(references)
        }
        scope.launch {
            for (ref in iterator) {
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
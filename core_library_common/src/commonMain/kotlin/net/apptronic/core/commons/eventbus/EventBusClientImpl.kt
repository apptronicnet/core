package net.apptronic.core.commons.eventbus

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.ObservableEntity

internal class EventBusClientImpl<T : Any> internal constructor(
    override val context: Context,
    private val bus: EventBusComponent,
    private val channelDescriptor: EventChannelDescriptor<T>
) : ObservableEntity<T>(), EventBusClient<T> {

    private val eventObserver = EventObserver<T>(context, channelDescriptor)
    override val observable: Observable<T> = eventObserver
    private var isActive = true

    init {
        bus.observeEvents(context, eventObserver)
        if (context.lifecycle.isTerminated()) {
            isActive = false
        } else {
            context.lifecycle.onExitFromActiveStage {
                isActive = false
            }
        }
    }

    override fun postEvent(event: T) {
        if (isActive) {
            bus.postEvent(EventTransportObject(channelDescriptor, event))
        }
    }

    override fun update(value: T) {
        postEvent(value)
    }

}
package net.apptronic.core.commons.eventbus

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.ObservableEntity

@UnderDevelopment
internal class EventBusClientImpl<T : Any> internal constructor(
        override val context: Context,
        private val bus: EventBusComponent,
        private val channelDescriptor: EventChannelDescriptor<T>
) : ObservableEntity<T>(), EventBusClient<T> {

    private val eventObserver = EventObserver<T>(context, channelDescriptor)
    override val observable: Observable<T> = eventObserver

    init {
        bus.observeEvents(context, eventObserver)
    }

    override fun postEvent(event: T) {
        bus.postEvent(EventTransportObject(channelDescriptor, event))
    }

    override fun update(value: T) {
        postEvent(value)
    }

}
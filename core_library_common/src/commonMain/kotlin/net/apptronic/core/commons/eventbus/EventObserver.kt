package net.apptronic.core.commons.eventbus

import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.base.subject.Subject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.ObservableEntity

internal class EventObserver<T : Any>(
        override val context: Context,
        private val channelDescriptor: EventChannelDescriptor<T>
) : ObservableEntity<T>() {

    override val observable: Subject<T> = PublishSubject()

    fun processEvent(eventTransportObject: EventTransportObject<*>) {
        if (channelDescriptor == eventTransportObject.channelDescriptor) {
            @Suppress("UNCHECKED_CAST")
            observable.update(eventTransportObject.event as T)
        }
    }

}
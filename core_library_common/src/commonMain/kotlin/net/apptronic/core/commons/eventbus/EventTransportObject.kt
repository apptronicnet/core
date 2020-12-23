package net.apptronic.core.commons.eventbus

internal data class EventTransportObject<T : Any>(
        val channelDescriptor: EventChannelDescriptor<T>,
        val event: T
)
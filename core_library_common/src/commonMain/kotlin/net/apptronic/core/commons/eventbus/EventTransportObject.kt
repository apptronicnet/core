package net.apptronic.core.commons.eventbus

import net.apptronic.core.UnderDevelopment

@UnderDevelopment
internal data class EventTransportObject<T : Any>(
        val channelDescriptor: EventChannelDescriptor<T>,
        val event: T
)
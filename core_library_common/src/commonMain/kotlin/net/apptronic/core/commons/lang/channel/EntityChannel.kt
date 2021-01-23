package net.apptronic.core.commons.lang.channel

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.entity.Entity

@UnderDevelopment
fun <T> Entity<T>.asChannel(
    capacity: Int = Channel.RENDEZVOUS,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
    onUndeliveredElement: ((T) -> Unit)? = null
): ReceiveChannel<T> {
    val channel = Channel<T>(capacity, onBufferOverflow, onUndeliveredElement)
    subscribeSuspend {
        channel.send(it)
    }
    return channel
}
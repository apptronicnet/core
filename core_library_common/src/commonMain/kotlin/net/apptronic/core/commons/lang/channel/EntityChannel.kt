package net.apptronic.core.commons.lang.channel

import kotlinx.coroutines.channels.Channel
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.entity.Entity

@UnderDevelopment
fun <T> Entity<T>.asChannel(): Channel<T> {
    val channel = Channel<T>()
    subscribeSuspend {
        channel.send(it)
    }
    return channel
}
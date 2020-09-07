package net.apptronic.core.lang.channel

import kotlinx.coroutines.channels.Channel
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.subscribeSuspend

fun <T> Entity<T>.asChannel(): Channel<T> {
    val channel = Channel<T>()
    subscribeSuspend {
        channel.send(it)
    }
    return channel
}
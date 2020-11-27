package net.apptronic.core.commons.eventbus

import net.apptronic.core.UnderDevelopment
import kotlin.reflect.KClass

@UnderDevelopment
inline fun <reified T : Any> eventChannel(name: String = ""): EventChannelDescriptor<T> {
    return eventChannel(T::class, name)
}

@UnderDevelopment
fun <T : Any> eventChannel(type: KClass<T>, name: String = ""): EventChannelDescriptor<T> {
    return EventChannelDescriptor(type, name)
}

@UnderDevelopment
class EventChannelDescriptor<T : Any> internal constructor(val type: KClass<T>, val name: String)
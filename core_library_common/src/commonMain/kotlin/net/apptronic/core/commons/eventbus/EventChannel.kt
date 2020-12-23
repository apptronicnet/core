package net.apptronic.core.commons.eventbus

import kotlin.reflect.KClass

inline fun <reified T : Any> eventChannel(name: String = ""): EventChannelDescriptor<T> {
    return eventChannel(T::class, name)
}

fun <T : Any> eventChannel(type: KClass<T>, name: String = ""): EventChannelDescriptor<T> {
    return EventChannelDescriptor(type, name)
}

class EventChannelDescriptor<T : Any> internal constructor(val type: KClass<T>, val name: String)
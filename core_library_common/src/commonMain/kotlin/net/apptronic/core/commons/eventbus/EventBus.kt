package net.apptronic.core.commons.eventbus

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.dependencyDescriptor

private val EventBusDescriptor = dependencyDescriptor<EventBusComponent>()
val DefaultEventChannel = eventChannel<Any>("Default")

/**
 * Place event bus in current [ModuleDefinition]
 */
@UnderDevelopment
fun ModuleDefinition.eventBus() {
    shared(EventBusDescriptor) {
        EventBusComponent(scopedContext())
    }
}

/**
 * Inject [EventBusClient] using default [EventChannelDescriptor]
 */
@UnderDevelopment
fun Contextual.eventBusClient(): EventBusClient<Any> {
    return eventBusClient(DefaultEventChannel)
}

/**
 * Inject [EventBusClient] using specific typed [EventChannelDescriptor]
 */
@UnderDevelopment
fun <T : Any> Contextual.eventBusClient(channelDescriptor: EventChannelDescriptor<T>): EventBusClient<T> {
    val bus = dependencyProvider.inject(EventBusDescriptor)
    return EventBusClientImpl(context, bus, channelDescriptor)
}
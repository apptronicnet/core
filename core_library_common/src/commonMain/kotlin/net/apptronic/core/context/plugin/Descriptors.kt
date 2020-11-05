package net.apptronic.core.context.plugin

import kotlin.reflect.KClass

inline fun <reified T : Plugin> pluginDescriptor(): PluginDescriptor<T> {
    return pluginDescriptor(T::class)
}

inline fun <reified T : Any> extensionDescriptor(): ExtensionDescriptor<T> {
    return extensionDescriptor(T::class)
}

fun <T : Plugin> pluginDescriptor(clz: KClass<T>): PluginDescriptor<T> {
    return PluginDescriptor(clz)
}

fun <T : Any> extensionDescriptor(clz: KClass<T>): ExtensionDescriptor<T> {
    return ExtensionDescriptor(clz)
}

/**
 * Defines type of [Plugin].
 */
class PluginDescriptor<T : Plugin> internal constructor(
        private val clz: KClass<T>
)

/**
 * Defines type of extension.
 */
class ExtensionDescriptor<T : Any> internal constructor(
        private val clz: KClass<T>
)
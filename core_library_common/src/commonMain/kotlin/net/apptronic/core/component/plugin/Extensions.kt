package net.apptronic.core.component.plugin

class Extensions {

    private val map = mutableMapOf<ExtensionDescriptor<*>, Any>()

    operator fun <T : Any> set(descriptor: ExtensionDescriptor<T>, extension: T) {
        map[descriptor] = extension
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(descriptor: ExtensionDescriptor<T>): T? {
        return map[descriptor] as? T
    }


}
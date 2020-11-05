package net.apptronic.core.context.plugin

class Extensions {

    private val map = mutableMapOf<ExtensionDescriptor<*>, Any>()

    operator fun <T : Any> set(descriptor: ExtensionDescriptor<T>, extension: T) {
        map[descriptor] = extension
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(descriptor: ExtensionDescriptor<T>): T? {
        return map[descriptor] as? T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> remove(descriptor: ExtensionDescriptor<T>): T? {
        return map.remove(descriptor) as? T
    }

    fun <T : Any> get(descriptor: ExtensionDescriptor<T>, provider: () -> T): T {
        return get(descriptor) ?: let {
            val ext = provider()
            set(descriptor, ext)
            ext
        }
    }

}
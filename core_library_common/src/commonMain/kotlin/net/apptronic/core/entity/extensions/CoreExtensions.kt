package net.apptronic.core.entity.extensions

import net.apptronic.core.entity.commons.Property

fun ifAllIsSet(vararg properties: Property<*>, action: () -> Unit) {
    if (properties.all { it.isSet() }) {
        action()
    }
}
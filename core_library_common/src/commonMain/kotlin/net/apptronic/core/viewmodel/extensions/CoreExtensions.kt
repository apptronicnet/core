package net.apptronic.core.viewmodel.extensions

import net.apptronic.core.entity.entities.Property

fun ifAllIsSet(vararg properties: Property<*>, action: () -> Unit) {
    if (properties.all { it.isSet() }) {
        action()
    }
}
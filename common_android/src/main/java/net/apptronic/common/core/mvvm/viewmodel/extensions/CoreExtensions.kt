package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.entities.Property

fun ifAllIsSet(vararg properties: Property<*>, action: () -> Unit) {
    if (properties.all { it.isSet() }) {
        action()
    }
}
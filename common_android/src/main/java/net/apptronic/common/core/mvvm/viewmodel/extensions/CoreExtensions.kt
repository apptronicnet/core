package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.entities.LiveModelProperty

fun ifAllIsSet(vararg properties: LiveModelProperty<*>, action: () -> Unit) {
    if (properties.all { it.isSet() }) {
        action()
    }
}
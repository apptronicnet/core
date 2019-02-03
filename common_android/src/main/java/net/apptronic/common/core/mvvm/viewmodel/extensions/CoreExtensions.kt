package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.ViewModelProperty

fun ifAllIsSet(vararg properties: ViewModelProperty<*>, action: () -> Unit) {
    if (properties.all { it.isSet() }) {
        action()
    }
}
package net.apptronic.common.android.ui.viewmodel.extensions

import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty

fun ifAllIsSet(vararg properties: ViewModelProperty<*>, action: () -> Unit) {
    if (properties.all { it.valueHolder != null }) {
        action()
    }
}
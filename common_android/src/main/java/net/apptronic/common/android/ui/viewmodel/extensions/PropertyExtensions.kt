package net.apptronic.common.android.ui.viewmodel.extensions

import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty

fun forEachChangeAnyOf(vararg properties: ViewModelProperty<*>, action: () -> Unit) {
    properties.forEach { property ->
        property.subscribe { _ ->
            ifAllIsSet(*properties) {
                action()
            }
        }
    }
}

fun <T> ViewModelProperty<T>.copyValueFrom(source: ViewModelProperty<T>): ViewModelProperty<T> {
    source.subscribe { set(it) }
    return this
}
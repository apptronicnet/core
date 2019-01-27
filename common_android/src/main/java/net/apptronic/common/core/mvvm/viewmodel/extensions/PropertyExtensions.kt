package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.mvvm.viewmodel.entity.ViewModelProperty

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
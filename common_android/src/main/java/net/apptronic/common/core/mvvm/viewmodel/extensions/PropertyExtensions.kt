package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.entities.LiveModelProperty

fun forEachChangeAnyOf(vararg properties: LiveModelProperty<*>, action: () -> Unit) {
    properties.forEach { property ->
        property.subscribe { _ ->
            ifAllIsSet(*properties) {
                action()
            }
        }
    }
}

fun <T> LiveModelProperty<T>.copyValueFrom(source: LiveModelProperty<T>): LiveModelProperty<T> {
    source.subscribe { set(it) }
    return this
}
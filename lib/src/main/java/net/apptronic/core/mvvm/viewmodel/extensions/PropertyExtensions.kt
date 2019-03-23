package net.apptronic.core.mvvm.viewmodel.extensions

import net.apptronic.core.component.entity.entities.Property

fun forEachChangeAnyOf(vararg properties: Property<*>, action: () -> Unit) {
    properties.forEach { property ->
        property.subscribe { _ ->
            ifAllIsSet(*properties) {
                action()
            }
        }
    }
}

fun <T> Property<T>.copyValueFrom(source: Property<T>): Property<T> {
    source.subscribe { set(it) }
    return this
}
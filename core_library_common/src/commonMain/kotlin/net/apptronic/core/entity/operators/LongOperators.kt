package net.apptronic.core.entity.operators

import net.apptronic.core.entity.base.Value

fun Value<Long>.increment() {
    update((getOrNull() ?: 0) + 1)
}

fun Value<Long>.decrement() {
    update((getOrNull() ?: 0) - 1)
}

fun Value<Long>.add(value: Long) {
    update((getOrNull() ?: 0) + value)
}

fun Value<Long>.subtract(value: Long) {
    update((getOrNull() ?: 0) - value)
}
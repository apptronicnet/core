package net.apptronic.core.component.entity.operators

import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.UpdateEntity

fun <T> T.increment() where T : UpdateEntity<Int>, T : EntityValue<Int> {
    update((getOrNull() ?: 0) + 1)
}

fun <T> T.decrement() where T : UpdateEntity<Int>, T : EntityValue<Int> {
    update((getOrNull() ?: 0) - 1)
}

fun <T> T.add(value: Int) where T : UpdateEntity<Int>, T : EntityValue<Int> {
    update((getOrNull() ?: 0) + value)
}

fun <T> T.subtract(value: Int) where T : UpdateEntity<Int>, T : EntityValue<Int> {
    update((getOrNull() ?: 0) - value)
}
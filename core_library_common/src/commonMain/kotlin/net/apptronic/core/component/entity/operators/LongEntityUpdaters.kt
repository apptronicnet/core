package net.apptronic.core.component.entity.operators

import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.UpdateEntity

fun <T> T.increment() where T : UpdateEntity<Long>, T : EntityValue<Long> {
    update((getOrNull() ?: 0) + 1)
}

fun <T> T.decrement() where T : UpdateEntity<Long>, T : EntityValue<Long> {
    update((getOrNull() ?: 0) - 1)
}

fun <T> T.add(value: Long) where T : UpdateEntity<Long>, T : EntityValue<Long> {
    update((getOrNull() ?: 0) + value)
}

fun <T> T.subtract(value: Long) where T : UpdateEntity<Long>, T : EntityValue<Long> {
    update((getOrNull() ?: 0) - value)
}
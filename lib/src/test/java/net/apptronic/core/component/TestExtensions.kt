package net.apptronic.core.component

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import kotlin.test.assertNull

private fun <T : Any> Entity<T>.value(): T {
    lateinit var value: T
    subscribe {
        value = it
    }
    return value
}

fun assert(entity: Entity<Boolean>) {
    assert(entity.value())
}

fun Entity<Boolean>.assertTrue() {
    assert(value())
}

fun Entity<Boolean>.assertFalse() {
    assert(value().not())
}

fun Entity<Boolean>.assertNull() {
    assertNull(value())
}
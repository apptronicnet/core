package net.apptronic.common.core.component

import net.apptronic.common.core.component.entity.Predicate
import kotlin.test.assertNull

private fun <T : Any> Predicate<T>.value(): T {
    lateinit var value: T
    subscribe {
        value = it
    }
    return value
}

fun assert(predicate: Predicate<Boolean>) {
    assert(predicate.value())
}

fun Predicate<Boolean>.assertTrue() {
    assert(value())
}

fun Predicate<Boolean>.assertFalse() {
    assert(value().not())
}

fun Predicate<Boolean>.assertNull() {
    assertNull(value())
}
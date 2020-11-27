package net.apptronic.core.entity

import net.apptronic.core.base.subject.ValueHolder
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private fun <T> Entity<T>.retrieveValue(): T {
    lateinit var valueHolder: ValueHolder<T>
    subscribe {
        valueHolder = ValueHolder(it)
    }
    return valueHolder.value
}

fun assert(entity: Entity<Boolean>) {
    assert(entity.retrieveValue())
}

fun Entity<Boolean>.assertTrue() {
    assert(retrieveValue())
}

fun Entity<Boolean>.assertFalse() {
    assert(retrieveValue().not())
}

fun <T> Entity<T?>.assertNull() {
    val value = retrieveValue()
    assertNull(value)
}

fun <T> Entity<T>.assertNoValue() {
    var valueHolder: ValueHolder<T>? = null
    subscribe {
        valueHolder = ValueHolder(it)
    }
    assertNull(valueHolder)
}

fun <T> Entity<T>.assertHasValue() {
    var valueHolder: ValueHolder<T>? = null
    subscribe {
        valueHolder = ValueHolder(it)
    }
    assertNotNull(valueHolder)
}

fun <T> Entity<T>.assertValueEquals(value: T) {
    assertEquals(value, retrieveValue())
}

fun <T> Entity<T>.assert(assertion: (T) -> Boolean) {
    assert(assertion(retrieveValue()))
}
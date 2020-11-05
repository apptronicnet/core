package net.apptronic.core.context.component

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.entity.Entity
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private fun <T> Entity<T>.value(): T {
    lateinit var valueHolder: ValueHolder<T>
    subscribe {
        valueHolder = ValueHolder(it)
    }
    return valueHolder.value
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

fun <T> Entity<T?>.assertNull() {
    val value = value()
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

fun <T> Entity<T>.assert(assertion: (T) -> Boolean) {
    assert(assertion(value()))
}
package net.apptronic.core.entity.operators

import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.base.SubjectEntity

fun <T> T.increment() where T : SubjectEntity<Int>, T : Property<Int> {
    update((getOrNull() ?: 0) + 1)
}

fun <T> T.decrement() where T : SubjectEntity<Int>, T : Property<Int> {
    update((getOrNull() ?: 0) - 1)
}

fun <T> T.add(value: Int) where T : SubjectEntity<Int>, T : Property<Int> {
    update((getOrNull() ?: 0) + value)
}

fun <T> T.subtract(value: Int) where T : SubjectEntity<Int>, T : Property<Int> {
    update((getOrNull() ?: 0) - value)
}
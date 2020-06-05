package net.apptronic.core

fun <T> assertEquals(left: List<T>, right: List<T>) {
    assert(left.size == right.size)
    left.indices.forEach {
        assert(left[it] == right[it])
    }
}
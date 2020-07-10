package net.apptronic.core

fun <T> assertListEquals(left: List<T>, right: List<T>) {
    assert(left.size == right.size)
    left.indices.forEach {
        assert(left[it] == right[it])
    }
}
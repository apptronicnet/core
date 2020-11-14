package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.FunctionProperty

/**
 * Transform collection to null if it is empty
 */
fun <T : Collection<E>, E> Entity<T>.nullIfEmpty(): FunctionProperty<T?> {
    return map {
        if (it.isNotEmpty()) it else null
    }
}

/**
 * Map each item of type [E] for [List] to another item of type [R]
 */
fun <E, R> Entity<List<E>>.mapItems(mapFunction: (E) -> R): FunctionProperty<List<R>> {
    return map { it.map(mapFunction) }
}

/**
 * Filter collection
 */
fun <E> Entity<List<E>>.filterItems(filterFunction: (E) -> Boolean): FunctionProperty<List<E>> {
    return map { it.filter(filterFunction) }
}

/**
 * Trim collection to desired length
 */
fun <E> Entity<List<E>>.trimLength(length: Int, ellipsizeItem: E? = null): FunctionProperty<List<E>> {
    return map {
        if (it.size <= length) {
            it
        } else {
            val trimLength = if (ellipsizeItem == null) length else length - 1
            mutableListOf<E>().apply {
                addAll(it.subList(0, trimLength))
                if (ellipsizeItem != null) {
                    add(ellipsizeItem)
                }
            }
        }
    }
}

fun <E> Entity<List<E>>.isEmpty(): FunctionProperty<Boolean> {
    return map {
        it.isEmpty()
    }
}

fun <E> Entity<List<E>>.isNotEmpty(): FunctionProperty<Boolean> {
    return map {
        it.isNotEmpty()
    }
}


fun <E> Entity<List<E?>>.removeNulls(): FunctionProperty<List<E>> {
    return map { list ->
        list.filter { item -> item != null }.map { item -> item!! }
    }
}
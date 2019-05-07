package net.apptronic.core.component.entity.extensions

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.variants.map

/**
 * Transform collection to null if it is empty
 */
fun <T : Collection<E>, E> Predicate<T>.nullIfEmpty(): Predicate<T?> {
    return map {
        if (it.isNotEmpty()) it else null
    }
}

/**
 * Map each item of type [E] for [List] to another item of type [R]
 */
fun <E, R> Predicate<List<E>>.mapItems(mapFunction: (E) -> R): Predicate<List<R>> {
    return map { it.map(mapFunction) }
}

/**
 * Filter collection
 */
fun <E> Predicate<List<E>>.filterItems(filterFunction: (E) -> Boolean): Predicate<List<E>> {
    return map { it.filter(filterFunction) }
}

/**
 * Trim collection to desired length
 */
fun <E> Predicate<List<E>>.trimLength(length: Int, ellipsizeItem: E? = null): Predicate<List<E>> {
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
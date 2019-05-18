package net.apptronic.core.component.entity.extensions

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.variants.map

/**
 * Transform collection to null if it is empty
 */
fun <T : Collection<E>, E> Entity<T>.nullIfEmpty(): Entity<T?> {
    return map {
        if (it.isNotEmpty()) it else null
    }
}

/**
 * Map each item of type [E] for [List] to another item of type [R]
 */
fun <E, R> Entity<List<E>>.mapItems(mapFunction: (E) -> R): Entity<List<R>> {
    return map { it.map(mapFunction) }
}

/**
 * Filter collection
 */
fun <E> Entity<List<E>>.filterItems(filterFunction: (E) -> Boolean): Entity<List<E>> {
    return map { it.filter(filterFunction) }
}

/**
 * Trim collection to desired length
 */
fun <E> Entity<List<E>>.trimLength(length: Int, ellipsizeItem: E? = null): Entity<List<E>> {
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
package net.apptronic.core.base.utils

interface EqComparator<T> {

    fun isEqualsNullable(left: T?, right: T?): Boolean {
        return left == null && right == null || (left != null && right != null && isEquals(left, right))
    }

    fun isEquals(left: T, right: T): Boolean

}

/**
 * Uses default language-level equals
 */
class SimpleEqComparator<T> : EqComparator<T> {

    override fun isEquals(left: T, right: T): Boolean {
        return left == right
    }

}

class ReferenceEqComparator<T> : EqComparator<T> {

    override fun isEquals(left: T, right: T): Boolean {
        return left === right
    }

}

/**
 * Never returns true
 */
class NeverEqComparator<T> : EqComparator<T> {

    override fun isEquals(left: T, right: T): Boolean {
        return false
    }

}

class ListEqComparator<T>(private val itemComparator: EqComparator<T> = SimpleEqComparator()) : EqComparator<List<T>> {

    override fun isEquals(left: List<T>, right: List<T>): Boolean {
        return left.size == right.size
                && left.mapIndexed { index, t ->
            itemComparator.isEquals(t, right[index])
        }.all { it }
    }

}

class SetEqComparator<T>(private val itemComparator: EqComparator<T> = SimpleEqComparator()) : EqComparator<Set<T>> {

    override fun isEquals(left: Set<T>, right: Set<T>): Boolean {
        return left.size == right.size
                && left.all { leftItem ->
            right.any { rightItem ->
                itemComparator.isEquals(leftItem, rightItem)
            }
        }
    }

}

class MapEqComparator<K, V>(private val itemComparator: EqComparator<V> = SimpleEqComparator()) : EqComparator<Map<K, V>> {

    private val keyComparator = SetEqComparator<K>()

    override fun isEquals(left: Map<K, V>, right: Map<K, V>): Boolean {
        return left.size == right.size
                && keyComparator.isEquals(left.keys, right.keys)
                && left.keys.all {
            itemComparator.isEqualsNullable(left[it], right[it])
        }
    }

}
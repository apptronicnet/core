package net.apptronic.core

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.subscriptions.EntitySubscriptionListener
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

inline fun <reified T> Entity<T>.record(): EntityRecorder<T> {
    return EntityRecorder(T::class.java, this)
}

class EntityRecorder<T> constructor(
        private val clazz: Class<T>,
        private val source: Entity<T>
) : EntitySubscriptionListener {

    private val items = mutableListOf<T>()
    private var isUnsubscribed = false
    private var last: ValueHolder<T>? = null

    init {
        val entitySubscription = source.subscribe {
            items.add(it)
            last = ValueHolder(it)
        }
        entitySubscription.registerListener(this)
    }

    override fun onUnsubscribed(subscription: EntitySubscription) {
        isUnsubscribed = true
    }

    val context: Context = source.context

    fun get(): List<T> {
        return items
    }

    operator fun get(index: Int): T {
        return items[index]
    }

    fun assertSize(size: Int) {
        assert(items.size == size) {
            "Expected size = $size when actual $this"
        }
    }

    fun assertItems(vararg expected: T) {
        assertItems(expected.toList())
    }

    fun assertItems(check: (Int, T) -> Boolean) {
        items.forEachIndexed { index, t ->
            assert(check(index, t)) {
                "Item $t at index $index does not match assertion"
            }
        }
    }

    fun assertItems(expected: List<T>) {
        val sizeEquals = items.size == expected.size
        val contentEquals = if (sizeEquals) {
            items.mapIndexed { index, value ->
                expected[index] == value
            }.all { it }
        } else {
            false
        }
        assert(sizeEquals && contentEquals) {
            val expectedText = expected.map { it.toString() }.joinToString(separator = ", ")
            val actualText = items.map { it.toString() }.joinToString(separator = ", ")
            "Content con equals:\n" +
                    "Expected is: size=${expected.size} [$expectedText]\n" +
                    "when actual: size=${items.size} [$actualText]"
        }
    }

    fun assertLast(value: T) {
        assertNotNull(last)
        assertEquals(value, last!!.value)
    }

    fun clear() {
        items.clear()
    }

    fun assertSubscribed() {
        assert(!isUnsubscribed) {
            "Already unsubscribed"
        }
    }

    fun assertUnsubscribed() {
        assert(isUnsubscribed) {
            "Still subscribed"
        }
    }

    override fun toString(): String {
        val values = items.map { it.toString() }.joinToString(separator = ", ")
        val status = if (isUnsubscribed) "Unsubscribed" else "Active"
        return "EntityRecorder${clazz.simpleName}" +
                " size=${items.size} $status" +
                "\n[$values]"
    }

}
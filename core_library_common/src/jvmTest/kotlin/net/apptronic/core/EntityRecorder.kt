package net.apptronic.core

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscriptions.EntitySubscriptionListener

inline fun <reified T> Entity<T>.record(): EntityRecorder<T> {
    return EntityRecorder(T::class.java, this)
}

class EntityRecorder<T> constructor(
        private val clazz: Class<T>,
        private val source: Entity<T>
) : Entity<Nothing>, EntitySubscriptionListener {

    private val items = mutableListOf<T>()
    private var isUnsubscribed = false

    init {
        val entitySubscription = source.subscribe {
            items.add(it)
        }
        entitySubscription.registerListener(this)
    }

    override fun onUnsubscribed(subscription: EntitySubscription) {
        isUnsubscribed = true
    }

    override val context: Context = source.context

    override fun subscribe(context: Context, observer: Observer<Nothing>): EntitySubscription {
        throw UnsupportedOperationException()
    }

    fun get(): List<T> {
        return items
    }

    fun assertSize(size: Int) {
        assert(items.size == size) {
            "Expected size = $size when actual $this"
        }
    }

    fun assertItems(vararg expected: T) {
        assertItems(expected.toList())
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
            val expectedText = items.map { it.toString() }.joinToString(separator = ", ")
            "Expected is size=${expected.size} [$expectedText] when actual $this"
        }
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
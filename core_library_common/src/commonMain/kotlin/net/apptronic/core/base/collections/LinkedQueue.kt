package net.apptronic.core.base.collections

import kotlinx.coroutines.CompletableDeferred

class LinkedQueue<T> {

    private class Node<T>(
            val value: T,
            var next: Node<T>?
    )

    private var awaiting = mutableListOf<CompletableDeferred<Unit>>()
    private var size = 0;
    private var start: Node<T>? = null
    private var end: Node<T>? = null

    fun take(): T? {
        val first = start
        return if (first != null) {
            start = first.next
            if (start == null) {
                end = null
            }
            size--
            first.value
        } else {
            null
        }
    }

    suspend fun takeAwait(): T {
        while (true) {
            val result = take()
            if (result != null) {
                return result
            }
            val deferred = CompletableDeferred<Unit>()
            awaiting.add(deferred)
            deferred.await()
        }
    }

    fun add(item: T) {
        size++
        val last = end
        if (last != null) {
            val next = Node(item, null)
            last.next = next
            end = next
        } else {
            start = Node(item, null)
            end = start
        }
        val awaiting = this.awaiting.toTypedArray()
        this.awaiting.clear()
        awaiting.forEach {
            it.complete(Unit)
        }
    }

    fun hasItems(): Boolean {
        return start != null
    }

    fun size(): Int {
        return size
    }

    fun trim(maxSize: Int) {
        while (size > maxSize) {
            take()
        }
    }

}
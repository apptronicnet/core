package net.apptronic.core.base.collections

class LinkedQueue<T> {

    private class Node<T>(
            val value: T,
            var next: Node<T>?
    )

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
package net.apptronic.core.base.collections

import kotlinx.coroutines.CompletableDeferred
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder

class LinkedQueue<T> : Queue<T>, Observer<T> {

    private class Node<T>(
            val value: T,
            var next: Node<T>?
    )

    private var awaiting = mutableListOf<CompletableDeferred<Unit>>()
    private var size = 0;
    private var start: Node<T>? = null
    private var end: Node<T>? = null
    private var consumer: Queue.Consumer<T>? = null

    override fun take(): ValueHolder<T>? {
        val first = start
        return if (first != null) {
            start = first.next
            if (start == null) {
                end = null
            }
            size--
            ValueHolder(first.value)
        } else {
            null
        }
    }

    override suspend fun takeAwait(): T {
        while (true) {
            val result = take()
            if (result != null) {
                return result.value
            }
            val deferred = CompletableDeferred<Unit>()
            awaiting.add(deferred)
            deferred.await()
        }
    }

    override fun add(item: T) {
        val consumer = this.consumer
        if (consumer != null) {
            consumer.onNext(item)
            return
        }
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

    override fun hasItems(): Boolean {
        return start != null
    }

    override fun size(): Int {
        return size
    }

    override fun trim(maxSize: Int, trimHandler: (T) -> Unit) {
        while (size > maxSize) {
            take()?.let {
                trimHandler(it.value)
            }
        }
    }

    override fun notify(value: T) {
        add(value)
    }

    override fun setConsumer(consumer: Queue.Consumer<T>) {
        do {
            val next = take()
            next?.let {
                consumer.onNext(it.value)
            }
        } while (next != null)
        this.consumer = consumer
    }

}
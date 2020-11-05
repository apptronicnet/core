package net.apptronic.core.base.collections

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.entity.Entity

fun <T> queueOf(): Queue<T> {
    return LinkedQueue()
}

fun <T> Entity<T>.asQueue(): Queue<T> {
    return LinkedQueue<T>().also {
        subscribe(it)
    }
}

fun <T> Queue<T>.setConsumer(onNextCallback: (T) -> Unit) {
    setConsumer(object : Queue.Consumer<T> {
        override fun onNext(next: T) {
            onNextCallback(next)
        }
    })
}

interface Queue<T> {

    interface Consumer<T> {

        fun onNext(next: T)

    }

    fun take(): ValueHolder<T>?

    suspend fun takeAwait(): T

    fun add(item: T)

    fun hasItems(): Boolean

    fun size(): Int

    fun trim(maxSize: Int, trimHandler: (T) -> Unit = {})

    /**
     * Set synchronous consumer for this [LinkedQueue]. Allows to execute [Queue.Consumer.onNext] for each item synchronously
     * on [add]. In this case queue will no be filled, methods [take] and [takeAwait] will never return items. For all
     * current items [Queue.Consumer.onNext] executed immediately.
     */
    fun setConsumer(consumer: Consumer<T>)

}
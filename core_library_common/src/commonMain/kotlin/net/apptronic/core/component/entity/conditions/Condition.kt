package net.apptronic.core.component.entity.conditions

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import net.apptronic.core.base.collections.queueOf
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.subscribe

fun <T> Entity<T>.createCondition(): Condition<T> {
    return Condition(this)
}

class Condition<T> internal constructor(val source: Entity<T>) {

    private val awaitContext = source.context.childContext()
    private val queue = queueOf<T>()
    private val isSetDeferred = CompletableDeferred<Unit>()
    private var current: ValueHolder<T>? = null

    init {
        source.subscribe(awaitContext) {
            queue.add(it)
            if (current == null) {
                isSetDeferred.complete(Unit)
            }
            current = ValueHolder((it))
        }
    }

    fun haveAny(): Boolean {
        return current != null
    }

    suspend fun awaitAny() {
        isSetDeferred.await()
    }

    fun current(): T {
        return current?.value ?: throw IllegalStateException("Nothing yet received")
    }

    suspend fun next(): T {
        return queue.takeAwait()
    }

    suspend fun awaitMatching(matchCondition: (T) -> Boolean) {
        do {
            val next = queue.takeAwait()
        } while (!matchCondition(next))
    }

    suspend fun awaitMatchingSuspend(matchCondition: suspend CoroutineScope.(T) -> Boolean) {
        do {
            val next = queue.takeAwait()
        } while (coroutineScope { !matchCondition(next) })
    }

    suspend fun nextMatching(matchCondition: (T) -> Boolean): T {
        var next: T
        do {
            next = queue.takeAwait()
        } while (!matchCondition(next))
        return next
    }

    suspend fun nextMatchingSuspend(matchCondition: suspend CoroutineScope.(T) -> Boolean): T {
        var next: T
        do {
            next = queue.takeAwait()
        } while (coroutineScope { !matchCondition(next) })
        return next
    }

    fun release() {
        awaitContext.terminate()
    }

}
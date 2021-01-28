package net.apptronic.core.base.lang

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import net.apptronic.core.base.subject.ValueHolder

/**
 * Creates [Attempt] object based on  [invocation] function
 */
fun <R> attempt(invocation: suspend CoroutineScope.() -> R) = Attempt(invocation)

class Attempt<R> internal constructor(val invocation: suspend CoroutineScope.() -> R) {

    /**
     * Invokes provided [invocation] inside try-catch block. In case of error suspends with [awaitForRetry] and repeats flow until full success.
     */
    suspend fun retryWhen(awaitForRetry: suspend CoroutineScope.(Exception) -> Unit): R {
        return coroutineScope {
            var result: ValueHolder<R>? = null
            do {
                try {
                    result = ValueHolder(invocation()).apply { }
                } catch (e: Exception) {
                    awaitForRetry(e)
                }
            } while (result == null)
            result.value
        }
    }

}
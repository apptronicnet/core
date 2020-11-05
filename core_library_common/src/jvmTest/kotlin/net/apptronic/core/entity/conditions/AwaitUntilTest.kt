package net.apptronic.core.entity.conditions

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import net.apptronic.core.context.component.value
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.testutils.createTestContext
import kotlin.test.Test

class AwaitUntilTest {

    val context = createTestContext()

    @Test
    fun verifyAwaitUntil() {
        val state = context.value<String>()
        val deferred = CompletableDeferred<Unit>()
        context.contextCoroutineScope.launch {
            state.awaitUntilCondition {
                it == "Completed"
            }
            deferred.complete(Unit)
        }
        assert(deferred.isCompleted.not())
        state.set("In progress")
        assert(deferred.isCompleted.not())
        state.set("Completed")
        assert(deferred.isCompleted)
    }

    @Test
    fun verifyAwaitUntilSuspend() {
        val state = context.value<String>()
        val deferred = CompletableDeferred<Unit>()
        context.contextCoroutineScope.launch {
            state.awaitUntilConditionSuspend {
                it == "Completed"
            }
            deferred.complete(Unit)
        }
        assert(deferred.isCompleted.not())
        state.set("In progress")
        assert(deferred.isCompleted.not())
        state.set("Completed")
        assert(deferred.isCompleted)
    }

    @Test
    fun verifyAwaitUntilValue() {
        val state = context.value<String>()
        val deferred = CompletableDeferred<Unit>()
        context.contextCoroutineScope.launch {
            state.awaitUntilValue("Completed")
            deferred.complete(Unit)
        }
        assert(deferred.isCompleted.not())
        state.set("In progress")
        assert(deferred.isCompleted.not())
        state.set("Completed")
        assert(deferred.isCompleted)
    }

    @Test
    fun verifyAwaitUntilTrue() {
        val state = context.value<Boolean>()
        val deferred = CompletableDeferred<Unit>()
        context.contextCoroutineScope.launch {
            state.awaitUntilTrue()
            deferred.complete(Unit)
        }
        assert(deferred.isCompleted.not())
        state.set(false)
        assert(deferred.isCompleted.not())
        state.set(true)
        assert(deferred.isCompleted)
    }

    @Test
    fun verifyAwaitUntilFalse() {
        val state = context.value<Boolean>()
        val deferred = CompletableDeferred<Unit>()
        context.contextCoroutineScope.launch {
            state.awaitUntilFalse()
            deferred.complete(Unit)
        }
        assert(deferred.isCompleted.not())
        state.set(true)
        assert(deferred.isCompleted.not())
        state.set(false)
        assert(deferred.isCompleted)
    }

}
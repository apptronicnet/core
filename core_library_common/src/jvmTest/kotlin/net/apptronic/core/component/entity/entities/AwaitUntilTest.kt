package net.apptronic.core.component.entity.entities

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.component.value
import net.apptronic.core.testutils.testContext
import kotlin.test.Test

class AwaitUntilTest {

    val context = testContext()

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
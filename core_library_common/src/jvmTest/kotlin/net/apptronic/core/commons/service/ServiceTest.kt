package net.apptronic.core.commons.service

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.dependencyModule
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertEquals

class ServiceTest {

    private val StringService = serviceDescriptor<String, Unit>()

    private val awaitSignals = mutableMapOf<String, CompletableDeferred<Unit>>()
    private val completeSignals = mutableMapOf<String, CompletableDeferred<Unit>>()
    private var instances = 0

    inner class ServiceExample(context: Context) : Service<String, Unit>(context) {
        init {
            instances++
        }

        override suspend fun onNext(request: String) {
            awaitSignals[request]!!.await()
            completeSignals[request]!!.complete(Unit)
        }
    }

    val context = testContext {
        dependencyModule {
            service(StringService) {
                ServiceExample(scopedContext())
            }
        }
    }

    private fun ServiceDispatcher<String, Unit>.nextRequest(request: String) {
        awaitSignals[request] = CompletableDeferred()
        completeSignals[request] = CompletableDeferred()
        postRequest(request)
    }

    @Test
    fun verifyServiceFlow() {
        val service = context.injectService(StringService)
        service.nextRequest("First")
        awaitSignals["First"]!!.complete(Unit)
        runBlocking {
            completeSignals["First"]!!.await()
        }
        assertEquals(instances, 1)

        service.nextRequest("Second")
        service.nextRequest("Third")
        service.nextRequest("Fourth")
        awaitSignals["Second"]!!.complete(Unit)
        awaitSignals["Third"]!!.complete(Unit)
        awaitSignals["Fourth"]!!.complete(Unit)
        runBlocking {
            completeSignals["Second"]!!.await()
            completeSignals["Third"]!!.await()
            completeSignals["Fourth"]!!.await()
        }
        assertEquals(instances, 2)
    }

}
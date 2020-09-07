package net.apptronic.core.commons.service

import kotlinx.coroutines.launch
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.dependencyModule
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ServiceOnErrorTest {

    private val Service = serviceDescriptor<String, Unit>()
    private val errors = mutableListOf<Exception>()

    inner class ServiceExample(context: Context) : Service<String, Unit>(context) {

        override suspend fun onNext(request: String) {
            throw RuntimeException("Error inside of service")
        }

        override fun onError(request: String, e: Exception) {
            try {
                super.onError(request, e)
            } catch (err: Exception) {
                errors.add(err)
            }
        }

    }

    val context = testContext {
        dependencyModule {
            service(Service) {
                ServiceExample(scopedContext())
            }
        }
    }

    @Test
    fun verifyErrorFlow() {
        val service = context.injectService(Service)
        context.contextCoroutineScope.launch {
            try {
                service.postRequest("Some")
                fail("Service should throw exception")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        assertEquals(errors.size, 1)
        val err = errors[0]
        err.printStackTrace()
        assertTrue(err is ServiceIsNotHandledErrorException)
        assertTrue(err.cause is RuntimeException)
        assertEquals(err.cause!!.message, "Error inside of service")
    }

}
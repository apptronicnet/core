package net.apptronic.core.commons.service

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.component.inject
import net.apptronic.core.context.component.value
import net.apptronic.core.context.coroutines.ManualDispatcher
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.testutils.createTestContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val ParseStringAsIntService = serviceDescriptor<String, Int>()

class ServiceResultTest {

    private val manualDispatcher = ManualDispatcher()

    private val successList = mutableListOf<Int>()
    private val errorList = mutableListOf<Exception>()

    private var launches = 0

    inner class ParseStringAsInt(context: Context) : Service<String, Int>(context) {

        init {
            launches++
        }

        override suspend fun onNext(request: String): Int {
            return withContext(manualDispatcher) {
                Integer.parseInt(request).also {
                    successList.add(it)
                }
            }
        }

        override fun onError(request: String, e: Exception) {
            errorList.add(e)
        }

    }

    val context = createTestContext {
        dependencyModule {
            service(ParseStringAsIntService) {
                ParseStringAsInt(scopedContext())
            }
            factory {
                UserComponent(scopedContext())
            }
        }
    }

    sealed class Response {
        data class Success(val value: Int) : Response()
        data class Error(val e: Exception) : Response()
    }

    class UserComponent(context: Context) : Component(context) {
        private val coroutineScope = contextCoroutineScope
        val service = injectService(ParseStringAsIntService)
        val source = value<String>()
        val isInProgress = value<Boolean>()
        val parsed = value<Response>()

        init {
            source.subscribe {
                coroutineScope.launch {
                    isInProgress.set(true)
                    val result = try {
                        Response.Success(service.sendRequest(it))
                    } catch (e: Exception) {
                        Response.Error(e)
                    } finally {
                        isInProgress.set(false)
                    }
                    parsed.set(result)
                }
            }
        }
    }

    @Test
    fun shouldReturnResults() {
        val user1 = context.inject<UserComponent>()
        assertEquals(0, launches)

        user1.source.set("1")
        assertTrue(user1.isInProgress.get())
        assertFalse(user1.parsed.isSet())

        manualDispatcher.runAll()

        assertFalse(user1.isInProgress.get())
        assertEquals(Response.Success(1), user1.parsed.get())

        assertEquals(1, launches)
        assertEquals(successList.size, 1)
        assertEquals(successList[0], 1)
        assertEquals(errorList.size, 0)

        user1.source.set("245")
        manualDispatcher.runAll()

        assertEquals(2, launches)
        assertEquals(successList.size, 2)
        assertEquals(successList[1], 245)
        assertEquals(errorList.size, 0)
    }

    @Test
    fun shouldReuseServiceForRequests() {
        val user1 = context.inject<UserComponent>()
        assertEquals(0, launches)

        user1.source.set("1")
        user1.source.set("2")
        user1.source.set("3")
        user1.source.set("4")
        user1.source.set("5")

        manualDispatcher.runAll()

        assertEquals(Response.Success(5), user1.parsed.get())

        assertEquals(1, launches)
        assertEquals(successList.size, 5)
        assertEquals(errorList.size, 0)
    }

    @Test
    fun shouldReturnErrorsCorrectly() {
        val user1 = context.inject<UserComponent>()
        user1.source.set("Not an int")

        manualDispatcher.runAll()

        assertTrue(user1.parsed.get() is Response.Error)
        assertEquals(1, launches)
        assertEquals(successList.size, 0)
        assertEquals(errorList.size, 1)
    }

    @Test
    fun shouldShareServiceForDifferentClients() {
        val user1 = context.inject<UserComponent>()
        val user2 = context.inject<UserComponent>()
        val user3 = context.inject<UserComponent>()
        val user4 = context.inject<UserComponent>()
        user1.source.set("11")
        user2.source.set("22")
        user3.source.set("33")
        user4.source.set("Not an int")

        manualDispatcher.runAll()

        assertEquals(Response.Success(11), user1.parsed.get())
        assertEquals(Response.Success(22), user2.parsed.get())
        assertEquals(Response.Success(33), user3.parsed.get())
        assertTrue(user4.parsed.get() is Response.Error)
        assertEquals(1, launches)
        assertEquals(successList.size, 3)
        assertEquals(errorList.size, 1)
    }

}
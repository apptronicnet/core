package net.apptronic.core.entity.exceptions

import net.apptronic.core.context.component.value
import net.apptronic.core.entity.entities.Property
import net.apptronic.core.entity.entities.asProperty
import net.apptronic.core.entity.functions.map
import net.apptronic.core.testutils.createTestContext
import org.junit.Test
import kotlin.test.*

class TryCatchingTest {

    val context = createTestContext()
    val source = context.value<String>()

    @Test
    fun shouldWorkNormally() {
        var e: Exception? = null
        val result = source.map {
            tryCatching {
                Integer.parseInt(it)
            }
        }.onException {
            e = it
        }.asProperty()

        source.set("1")
        assertEquals(result.get(), 1)
        assertNull(e)
    }

    @Test
    fun shouldHandleException() {
        var e: Exception? = null
        val result = source.map {
            tryCatching {
                Integer.parseInt(it)
            }
        }.onException {
            e = it
        }.asProperty()

        source.set("Not an int")
        assertFalse(result.isSet())
        assertNotNull(e)
    }

    private class ThrownException(val cause: Exception)

    @Test
    fun shouldMapException() {
        val result = source.map {
            tryCatching {
                Integer.parseInt(it) as Any
            }
        }.mapException {
            ThrownException(it)
        }.asProperty()

        source.set("Not an int")
        assertTrue(result.get() is ThrownException)
    }

    @Test
    fun shouldSendException() {
        val ex = context.value<Exception>()
        val result = source.map {
            tryCatching {
                Integer.parseInt(it)
            }
        }.sendException(ex).asProperty()

        source.set("Not an int")
        assertFalse(result.isSet())
        assertTrue(ex.isSet())
    }

    @Test
    fun shouldRecallCatchAndFinally() {
        var catchCalledTimes = 0
        var finallyCalledTimes = 0
        val result: Property<TryCatchResult<Int>> = source.map {
            tryCatching {
                Integer.parseInt(it)
            }.catchException {
                catchCalledTimes++
            }.doFinally {
                finallyCalledTimes++
            }
        }.asProperty()

        source.set("1")
        assertEquals(result.get(), TryCatchResult.Success(1))
        assertEquals(catchCalledTimes, 0)
        assertEquals(finallyCalledTimes, 1)

        source.set("Not an number")
        assertTrue(result.get() is TryCatchResult.Failure)
        assertEquals(catchCalledTimes, 1)
        assertEquals(finallyCalledTimes, 2)
    }

    @Test
    fun shouldReturnFallbackValue() {
        val result = source.map {
            tryCatching {
                Integer.parseInt(it)
            }.onErrorReturn(999)
        }.asProperty()

        source.set("1")
        assertEquals(result.get(), 1)

        source.set("Not an number")
        assertEquals(result.get(), 999)
    }

    @Test
    fun shouldReturnFallbackNull() {
        val result = source.map {
            tryCatching {
                Integer.parseInt(it)
            }.onErrorReturnNull()
        }.asProperty()

        source.set("1")
        assertEquals(result.get(), 1)

        source.set("Not an number")
        assertNull(result.get())
    }

    @Test
    fun shouldReturnFallbackFromProvider() {
        val result = source.map {
            tryCatching {
                Integer.parseInt(it)
            }.onErrorProvide {
                2
            }
        }.asProperty()

        source.set("1")
        assertEquals(result.get(), 1)

        source.set("Not an number")
        assertEquals(result.get(), 2)
    }

}
package net.apptronic.core.context.di

import junit.framework.Assert.assertFalse
import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.dependencyModule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SingleInitOnLoadTest : BaseContextTest() {

    var isShouldBeNotInitializedCount = 0
    var isShouldBeNotInitialized = false
    var isExpectedInitializedCount = 0
    var isExpectedInitialized = false

    inner class SomeNoInitOnLoad {

        init {
            isShouldBeNotInitializedCount++
            isShouldBeNotInitialized = true
        }

    }

    inner class SomeInitOnLoad {

        init {
            isExpectedInitializedCount++
            isExpectedInitialized = true
        }

    }

    @Test
    fun verify() {
        context.dependencyModule {
            single {
                SomeNoInitOnLoad()
            }
            single {
                SomeInitOnLoad()
            }.initOnLoad()
        }

        assertFalse(isShouldBeNotInitialized)
        assertTrue(isExpectedInitialized)
        assertEquals(0, isShouldBeNotInitializedCount)
        assertEquals(1, isExpectedInitializedCount)

        dependencyProvider.inject<SomeNoInitOnLoad>()

        assertTrue(isShouldBeNotInitialized)
        assertTrue(isExpectedInitialized)
        assertEquals(1, isShouldBeNotInitializedCount)
        assertEquals(1, isExpectedInitializedCount)

        dependencyProvider.inject<SomeInitOnLoad>()

        assertTrue(isShouldBeNotInitialized)
        assertTrue(isExpectedInitialized)
        assertEquals(1, isShouldBeNotInitializedCount)
        assertEquals(1, isExpectedInitializedCount)
    }

}
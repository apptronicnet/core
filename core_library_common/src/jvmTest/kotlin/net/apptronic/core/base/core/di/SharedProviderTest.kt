package net.apptronic.core.base.core.di

import junit.framework.Assert.fail
import net.apptronic.core.testutils.TestContext
import org.junit.Test

class SharedProviderTest {

    val coreContext = TestContext()

    @Test
    fun notWrittenYet() {
        fail("Not written yet")
    }

    @Test
    fun traceTest() {
        coreContext.dependencyDispatcher.traceDependencyTree().print()
    }

}
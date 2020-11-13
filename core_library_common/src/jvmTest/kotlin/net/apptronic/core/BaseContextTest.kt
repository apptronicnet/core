package net.apptronic.core

import net.apptronic.core.context.Contextual
import net.apptronic.core.context.terminate
import net.apptronic.core.test.testContext
import org.junit.After

abstract class BaseContextTest : Contextual {

    override val context = testContext()

    @After
    fun after() {
        context.terminate()
    }

}
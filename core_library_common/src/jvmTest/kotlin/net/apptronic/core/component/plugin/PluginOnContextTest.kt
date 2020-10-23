package net.apptronic.core.component.plugin

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.coreContext
import net.apptronic.core.component.coroutines.testCoroutineDispatchers
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class PluginOnContextTest {

    private val installedIn = mutableListOf<Context>()
    private val handledIn = mutableListOf<Context>()

    private val descriptor = pluginDescriptor<OnInstallContextPlugin>()

    inner class OnInstallContextPlugin : Plugin {

        override fun onInstall(context: Context) {
            super.onInstall(context)
            installedIn.add(context)
        }

        override fun onContext(context: Context) {
            super.onContext(context)
            handledIn.add(context)
        }

    }

    private val rootContext = coreContext(testCoroutineDispatchers()) {
        installPlugin(descriptor, OnInstallContextPlugin())
    }

    private val child1 = rootContext.childContext()
    private val child2 = rootContext.childContext()
    private val child3 = rootContext.childContext()
    private val child4 = child1.childContext()
    private val child5 = child1.childContext()
    private val child6 = child2.childContext()

    @Test
    fun verifyInstallCalledCorrectly() {
        assertEquals(installedIn.size, 1)
        assertSame(installedIn[0], rootContext)
    }

    @Test
    fun verifyHandleCalledCorrectly() {
        assertEquals(handledIn.size, 7)
        assertSame(handledIn[0], rootContext)
        assertSame(handledIn[1], child1)
        assertSame(handledIn[2], child2)
        assertSame(handledIn[3], child3)
        assertSame(handledIn[4], child4)
        assertSame(handledIn[5], child5)
        assertSame(handledIn[6], child6)
    }

}
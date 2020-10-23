package net.apptronic.core.component.plugin

import net.apptronic.core.component.Component
import net.apptronic.core.component.IComponent
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.coreContext
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.coroutines.testCoroutineDispatchers
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class PluginOnComponentTest {

    private val onComponents = mutableListOf<IComponent>()

    private val descriptor = pluginDescriptor<OnComponentPlugin>()

    inner class OnComponentPlugin : Plugin {

        override fun onComponent(component: IComponent) {
            super.onComponent(component)
            onComponents.add(component)
        }

    }

    private val rootContext = coreContext(testCoroutineDispatchers()) {
        installPlugin(descriptor, OnComponentPlugin())
    }

    private val child1 = rootContext.childContext()
    private val component0 = Component(child1)
    private val component1 = Component(child1)
    private val child2 = rootContext.childContext()
    private val component2 = Component(child2)
    private val component3 = Component(child2)
    private val child3 = rootContext.childContext()
    private val component4 = Component(child3)
    private val child4 = child1.childContext()
    private val component5 = Component(child4)
    private val child5 = child1.childContext()
    private val component6 = Component(child5)
    private val child6 = child2.viewModelContext()
    private val viewModel7 = Component(child5)
    private val viewModel8 = Component(child5)
    private val child7 = child6.viewModelContext()
    private val viewModel9 = Component(child7)
    private val viewModel10 = Component(child7)

    @Test
    fun verifyInstallCalledCorrectly() {
        assertEquals(onComponents.size, 11)
        assertSame(onComponents[0], component0)
        assertSame(onComponents[1], component1)
        assertSame(onComponents[2], component2)
        assertSame(onComponents[3], component3)
        assertSame(onComponents[4], component4)
        assertSame(onComponents[5], component5)
        assertSame(onComponents[6], component6)
        assertSame(onComponents[7], viewModel7)
        assertSame(onComponents[8], viewModel8)
        assertSame(onComponents[9], viewModel9)
        assertSame(onComponents[10], viewModel10)
    }

}
package net.apptronic.core.context.plugin

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.coreContext
import net.apptronic.core.context.terminate
import net.apptronic.core.test.testCoroutineDispatchers
import org.junit.After

abstract class PluginOnDependencyTest : Plugin, Contextual {

    internal var enableProvideWrap = false
    internal var enableInjectWrap = false

    internal interface SomeDependency

    internal class SomeDependencyImpl : SomeDependency

    internal class ProvideWrapper(val wrapped: SomeDependency) : SomeDependency

    internal class InjectWrapper(val wrapped: SomeDependency) : SomeDependency

    private val descriptor = pluginDescriptor<PluginOnDependencyTest>()

    final override val context: Context = coreContext(testCoroutineDispatchers()) {
        installPlugin(descriptor, this@PluginOnDependencyTest)
    }

    override fun <T> onProvide(definitionContext: Context, instance: T): T {
        if (enableProvideWrap && instance is SomeDependency) {
            return ProvideWrapper(instance) as T
        }
        return super.onProvide(definitionContext, instance)
    }

    override fun <T> onInject(definitionContext: Context, injectionContext: Context, instance: T): T {
        if (enableInjectWrap && instance is SomeDependency) {
            return InjectWrapper(instance) as T
        }
        return super.onInject(definitionContext, injectionContext, instance)
    }

    @After
    fun after() {
        context.terminate()
    }

}
package net.apptronic.core.context.plugin

import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.di.dependencyDescriptor
import org.junit.Test
import kotlin.test.assertNotSame

class PluginOnDependencyFactoryDescriptorTest : PluginOnDependencyTest() {

    private val descriptor = dependencyDescriptor<SomeDependency>()

    init {
        context.dependencyModule {
            factory(descriptor) {
                SomeDependencyImpl()
            }
        }
    }

    @Test
    fun verifyInject() {
        val instance1 = dependencyProvider.inject(descriptor) as SomeDependencyImpl
        val instance2 = dependencyProvider.inject(descriptor) as SomeDependencyImpl
        val instance3 = dependencyProvider.inject(descriptor) as SomeDependencyImpl

        assertNotSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

    @Test
    fun verifyProvideWrap() {
        enableProvideWrap = true

        val provide1 = dependencyProvider.inject(descriptor) as ProvideWrapper
        val provide2 = dependencyProvider.inject(descriptor) as ProvideWrapper
        val provide3 = dependencyProvider.inject(descriptor) as ProvideWrapper

        assertNotSame(provide1, provide2)
        assertNotSame(provide1, provide3)
        assertNotSame(provide2, provide3)

        val instance1 = provide1.wrapped as SomeDependencyImpl
        val instance2 = provide2.wrapped as SomeDependencyImpl
        val instance3 = provide3.wrapped as SomeDependencyImpl

        assertNotSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

    @Test
    fun verifyInjectedWrap() {
        enableInjectWrap = true

        val inject1 = dependencyProvider.inject(descriptor) as InjectWrapper
        val inject2 = dependencyProvider.inject(descriptor) as InjectWrapper
        val inject3 = dependencyProvider.inject(descriptor) as InjectWrapper

        assertNotSame(inject1, inject2)
        assertNotSame(inject1, inject3)
        assertNotSame(inject2, inject3)

        val instance1 = inject1.wrapped as SomeDependencyImpl
        val instance2 = inject2.wrapped as SomeDependencyImpl
        val instance3 = inject3.wrapped as SomeDependencyImpl

        assertNotSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

    @Test
    fun verifyAllWrap() {
        enableProvideWrap = true
        enableInjectWrap = true

        val inject1 = dependencyProvider.inject(descriptor) as InjectWrapper
        val inject2 = dependencyProvider.inject(descriptor) as InjectWrapper
        val inject3 = dependencyProvider.inject(descriptor) as InjectWrapper

        assertNotSame(inject1, inject2)
        assertNotSame(inject1, inject3)
        assertNotSame(inject2, inject3)

        val provide1 = inject1.wrapped as ProvideWrapper
        val provide2 = inject2.wrapped as ProvideWrapper
        val provide3 = inject3.wrapped as ProvideWrapper

        assertNotSame(provide1, provide2)
        assertNotSame(provide1, provide3)
        assertNotSame(provide2, provide3)

        val instance1 = provide1.wrapped as SomeDependencyImpl
        val instance2 = provide2.wrapped as SomeDependencyImpl
        val instance3 = provide3.wrapped as SomeDependencyImpl

        assertNotSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

}
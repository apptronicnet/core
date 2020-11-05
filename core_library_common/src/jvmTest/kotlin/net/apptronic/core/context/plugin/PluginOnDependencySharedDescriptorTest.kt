package net.apptronic.core.context.plugin

import net.apptronic.core.context.component.inject
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.di.dependencyDescriptor
import net.apptronic.core.context.di.parameters
import org.junit.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class PluginOnDependencySharedDescriptorTest : PluginOnDependencyTest() {

    private val descriptor = dependencyDescriptor<SomeDependency>()

    init {
        context.dependencyModule {
            shared(descriptor) {
                SomeDependencyImpl()
            }
        }
    }

    @Test
    fun verifyInject() {
        val instance1 = inject(descriptor, parameters { add(1) }) as SomeDependencyImpl
        val instance2 = inject(descriptor, parameters { add(1) }) as SomeDependencyImpl
        val instance3 = inject(descriptor, parameters { add(2) }) as SomeDependencyImpl

        assertSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

    @Test
    fun verifyProvideWrap() {
        enableProvideWrap = true

        val provide1 = inject(descriptor, parameters { add(1) }) as ProvideWrapper
        val provide2 = inject(descriptor, parameters { add(1) }) as ProvideWrapper
        val provide3 = inject(descriptor, parameters { add(2) }) as ProvideWrapper

        assertSame(provide1, provide2)
        assertNotSame(provide1, provide3)
        assertNotSame(provide2, provide3)

        val instance1 = provide1.wrapped as SomeDependencyImpl
        val instance2 = provide2.wrapped as SomeDependencyImpl
        val instance3 = provide3.wrapped as SomeDependencyImpl

        assertSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

    @Test
    fun verifyInjectedWrap() {
        enableInjectWrap = true

        val inject1 = inject(descriptor, parameters { add(1) }) as InjectWrapper
        val inject2 = inject(descriptor, parameters { add(1) }) as InjectWrapper
        val inject3 = inject(descriptor, parameters { add(2) }) as InjectWrapper

        assertNotSame(inject1, inject2)
        assertNotSame(inject1, inject3)
        assertNotSame(inject2, inject3)

        val instance1 = inject1.wrapped as SomeDependencyImpl
        val instance2 = inject2.wrapped as SomeDependencyImpl
        val instance3 = inject3.wrapped as SomeDependencyImpl

        assertSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

    @Test
    fun verifyAllWrap() {
        enableProvideWrap = true
        enableInjectWrap = true

        val inject1 = inject(descriptor, parameters { add(1) }) as InjectWrapper
        val inject2 = inject(descriptor, parameters { add(1) }) as InjectWrapper
        val inject3 = inject(descriptor, parameters { add(2) }) as InjectWrapper

        assertNotSame(inject1, inject2)
        assertNotSame(inject1, inject3)
        assertNotSame(inject2, inject3)

        val provide1 = inject1.wrapped as ProvideWrapper
        val provide2 = inject2.wrapped as ProvideWrapper
        val provide3 = inject3.wrapped as ProvideWrapper

        assertSame(provide1, provide2)
        assertNotSame(provide1, provide3)
        assertNotSame(provide2, provide3)

        val instance1 = provide1.wrapped as SomeDependencyImpl
        val instance2 = provide2.wrapped as SomeDependencyImpl
        val instance3 = provide3.wrapped as SomeDependencyImpl

        assertSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

}
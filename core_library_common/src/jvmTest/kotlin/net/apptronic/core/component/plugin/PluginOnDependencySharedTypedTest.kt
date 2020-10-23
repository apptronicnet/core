package net.apptronic.core.component.plugin

import net.apptronic.core.component.context.dependencyModule
import net.apptronic.core.component.di.parameters
import net.apptronic.core.component.inject
import org.junit.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class PluginOnDependencySharedTypedTest : PluginOnDependencyTest() {

    init {
        context.dependencyModule {
            shared<SomeDependency> {
                SomeDependencyImpl()
            }
        }
    }

    @Test
    fun verifyInject() {
        val instance1 = inject<SomeDependency>(parameters { add(1) }) as SomeDependencyImpl
        val instance2 = inject<SomeDependency>(parameters { add(1) }) as SomeDependencyImpl
        val instance3 = inject<SomeDependency>(parameters { add(2) }) as SomeDependencyImpl

        assertSame(instance1, instance2)
        assertNotSame(instance1, instance3)
        assertNotSame(instance2, instance3)
    }

    @Test
    fun verifyProvideWrap() {
        enableProvideWrap = true

        val provide1 = inject<SomeDependency>(parameters { add(1) }) as ProvideWrapper
        val provide2 = inject<SomeDependency>(parameters { add(1) }) as ProvideWrapper
        val provide3 = inject<SomeDependency>(parameters { add(2) }) as ProvideWrapper

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

        val inject1 = inject<SomeDependency>(parameters { add(1) }) as InjectWrapper
        val inject2 = inject<SomeDependency>(parameters { add(1) }) as InjectWrapper
        val inject3 = inject<SomeDependency>(parameters { add(2) }) as InjectWrapper

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

        val inject1 = inject<SomeDependency>(parameters { add(1) }) as InjectWrapper
        val inject2 = inject<SomeDependency>(parameters { add(1) }) as InjectWrapper
        val inject3 = inject<SomeDependency>(parameters { add(2) }) as InjectWrapper

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
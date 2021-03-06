package net.apptronic.core.context.di

import net.apptronic.core.testutils.BaseTestComponent
import org.junit.Test

class BasicProvidersTest {

    interface ManyInstances

    class ManyInstancesImpl : ManyInstances {

        var isCleared = false

        fun clear() {
            isCleared = true
        }

    }

    interface SingleInstance

    class SingleInstanceImpl : SingleInstance {

        var clearedCalls = 0

        fun clear() {
            clearedCalls++
        }

    }

    private companion object {

        val sampleModule = declareModule {
            factory<ManyInstances> {
                ManyInstancesImpl().onRecycle {
                    it.clear()
                }
            }
            single<SingleInstance> {
                SingleInstanceImpl().onRecycle {
                    it.clear()
                }
            }
        }

    }

    private class TestComponent : BaseTestComponent(
            contextInitializer = {
                dependencyDispatcher.addModule(sampleModule)
            }
    ) {

        val manyInstances1: ManyInstances = inject()
        val manyInstances2: ManyInstances = inject()
        val manyInstances3: ManyInstances = inject()

        val singleInstance1: SingleInstance = inject()
        val singleInstance2: SingleInstance = inject()
        val singleInstance3: SingleInstance = inject()

    }

    private val component = TestComponent()

    @Test
    fun manyInstancesShouldBeDifferent() {
        component.apply {
            assert(manyInstances1 is ManyInstancesImpl)
            assert(manyInstances2 is ManyInstancesImpl)
            assert(manyInstances3 is ManyInstancesImpl)
            assert(manyInstances1 != manyInstances2)
            assert(manyInstances2 != manyInstances3)
            assert(manyInstances1 != manyInstances3)
        }
    }

    @Test
    fun singleInstanceShouldBeSame() {
        component.apply {
            assert(singleInstance1 is SingleInstanceImpl)
            assert(singleInstance2 is SingleInstanceImpl)
            assert(singleInstance3 is SingleInstanceImpl)
            assert(singleInstance1 == singleInstance2)
            assert(singleInstance1 == singleInstance3)
        }
    }

    @Test
    fun shouldRecycleAll() {
        component.apply {
            getLifecycle().terminate()
            assert((singleInstance1 as SingleInstanceImpl).clearedCalls == 1)
            assert((manyInstances1 as ManyInstancesImpl).isCleared)
            assert((manyInstances2 as ManyInstancesImpl).isCleared)
            assert((manyInstances3 as ManyInstancesImpl).isCleared)
        }
    }

    @Test
    fun traceTest() {
        component.context.dependencyDispatcher.traceDependencyTree().print()
    }

}
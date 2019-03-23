package net.apptronic.common.core.di

import net.apptronic.common.utils.BaseTestComponent
import net.apptronic.core.component.di.declareModule
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

    companion object {

        val sampleModule = declareModule {
            factory<ManyInstances> {
                ManyInstancesImpl()
            }.onRecycle {
                (it as ManyInstancesImpl).clear()
            }
            single<SingleInstance> {
                SingleInstanceImpl()
            }.onRecycle {
                (it as SingleInstanceImpl).clear()
            }
        }

    }

    class TestComponent : BaseTestComponent(
        contextInitializer = {
            objects().addModule(sampleModule)
        }
    ) {

        val manyInstances1: ManyInstances = objects().get()
        val manyInstances2: ManyInstances = objects().get()
        val manyInstances3: ManyInstances = objects().get()

        val singleInstance1: SingleInstance = objects().get()
        val singleInstance2: SingleInstance = objects().get()
        val singleInstance3: SingleInstance = objects().get()

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

}
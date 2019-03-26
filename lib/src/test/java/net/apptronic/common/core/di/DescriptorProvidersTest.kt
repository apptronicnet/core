package net.apptronic.common.core.di

import net.apptronic.common.utils.BaseTestComponent
import net.apptronic.core.component.di.Descriptor
import net.apptronic.core.component.di.declareModule
import org.junit.Test

class DescriptorProvidersTest {

    interface MultiInterface

    class MultiBase : MultiInterface
    class MultiOne : MultiInterface
    class MultiTwo : MultiInterface

    interface SingleInterface

    class SingleBase : SingleInterface
    class SingleOne : SingleInterface
    class SingleTwo : SingleInterface

    private companion object {

        val MultiOneDescriptor = Descriptor<MultiInterface>()
        val MultiTwoDescriptor = Descriptor<MultiInterface>()
        val SingleOneDescriptor = Descriptor<SingleInterface>()
        val SingleTwoDescriptor = Descriptor<SingleInterface>()

        private val sampleModule = declareModule {

            factory<MultiInterface> {
                MultiBase()
            }

            factory(MultiOneDescriptor) {
                MultiOne()
            }

            factory(MultiTwoDescriptor) {
                MultiTwo()
            }

            single<SingleInterface> {
                SingleBase()
            }

            single(SingleOneDescriptor) {
                SingleOne()
            }

            single(SingleTwoDescriptor) {
                SingleTwo()
            }

        }

    }

    private class TestComponent : BaseTestComponent(
        contextInitializer = {
            objects().addModule(sampleModule)
        }
    ) {

        val multiBase_1 = objects().get<MultiInterface>()
        val multiBase_2 = objects().get<MultiInterface>()

        val multiOne_1 = objects().get(MultiOneDescriptor)
        val multiOne_2 = objects().get(MultiOneDescriptor)

        val multiTwo_1 = objects().get(MultiTwoDescriptor)
        val multiTwo_2 = objects().get(MultiTwoDescriptor)

        val singleBase_1 = objects().get<SingleInterface>()
        val singleBase_2 = objects().get<SingleInterface>()

        val singleOne_1 = objects().get(SingleOneDescriptor)
        val singleOne_2 = objects().get(SingleOneDescriptor)

        val singleTwo_1 = objects().get(SingleTwoDescriptor)
        val singleTwo_2 = objects().get(SingleTwoDescriptor)

    }

    private val component = TestComponent()

    @Test
    fun multiShouldBeExactClass() {
        component.apply {
            assert(multiBase_1 is MultiBase)
            assert(multiBase_2 is MultiBase)

            assert(multiOne_1 is MultiOne)
            assert(multiOne_2 is MultiOne)

            assert(multiTwo_1 is MultiTwo)
            assert(multiTwo_2 is MultiTwo)
        }
    }

    @Test
    fun multiShouldBeDifferent() {
        component.apply {
            assert(multiBase_1 !== multiBase_2)
            assert(multiOne_1 !== multiOne_2)
            assert(multiTwo_1 !== multiTwo_2)
        }
    }

    @Test
    fun singleShouldBeExactClass() {
        component.apply {
            assert(singleBase_1 is SingleBase)
            assert(singleBase_2 is SingleBase)

            assert(singleOne_1 is SingleOne)
            assert(singleOne_2 is SingleOne)

            assert(singleTwo_1 is SingleTwo)
            assert(singleTwo_2 is SingleTwo)
        }
    }

    @Test
    fun singleShouldBeSame() {
        component.apply {
            assert(singleBase_1 === singleBase_2)
            assert(singleOne_1 === singleOne_2)
            assert(singleTwo_1 === singleTwo_2)
        }
    }

}
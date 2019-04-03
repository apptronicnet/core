package net.apptronic.common.core.di

import net.apptronic.common.utils.BaseTestComponent
import net.apptronic.core.component.di.createDescriptor
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

        val MultiOneDescriptor = createDescriptor<MultiInterface>()
        val MultiTwoDescriptor = createDescriptor<MultiInterface>()
        val SingleOneDescriptor = createDescriptor<SingleInterface>()
        val SingleTwoDescriptor = createDescriptor<SingleInterface>()

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
            getProvider().addModule(sampleModule)
        }
    ) {

        val multiBase_1 = getProvider().inject<MultiInterface>()
        val multiBase_2 = getProvider().inject<MultiInterface>()

        val multiOne_1 = getProvider().inject(MultiOneDescriptor)
        val multiOne_2 = getProvider().inject(MultiOneDescriptor)

        val multiTwo_1 = getProvider().inject(MultiTwoDescriptor)
        val multiTwo_2 = getProvider().inject(MultiTwoDescriptor)

        val singleBase_1 = getProvider().inject<SingleInterface>()
        val singleBase_2 = getProvider().inject<SingleInterface>()

        val singleOne_1 = getProvider().inject(SingleOneDescriptor)
        val singleOne_2 = getProvider().inject(SingleOneDescriptor)

        val singleTwo_1 = getProvider().inject(SingleTwoDescriptor)
        val singleTwo_2 = getProvider().inject(SingleTwoDescriptor)

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
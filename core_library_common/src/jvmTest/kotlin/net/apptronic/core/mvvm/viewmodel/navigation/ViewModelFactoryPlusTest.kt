package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.testutils.testContext
import org.junit.Test

class ViewModelFactoryPlusTest {

    private val context = testContext()

    class IntViewModel(context: ViewModelContext, val id: Int) : ViewModel(context)
    class StringViewModel(context: ViewModelContext, val id: String) : ViewModel(context)
    class BooleanViewModel(context: ViewModelContext, val id: Boolean) : ViewModel(context)

    object IntBuilder : ViewModelBuilder<Int, Int, IntViewModel> {
        override fun getId(item: Int): Int = item
        override fun onCreateViewModel(parent: Context, item: Int): IntViewModel {
            return IntViewModel(parent.viewModelContext(), item)
        }
    }

    object StringBuilder : ViewModelBuilder<String, String, StringViewModel> {
        override fun getId(item: String): String = item
        override fun onCreateViewModel(parent: Context, item: String): StringViewModel {
            return StringViewModel(parent.viewModelContext(), item)
        }
    }

    object BooleanBuilder : ViewModelBuilder<Boolean, Boolean, BooleanViewModel> {
        override fun getId(item: Boolean): Boolean = item
        override fun onCreateViewModel(parent: Context, item: Boolean): BooleanViewModel {
            return BooleanViewModel(parent.viewModelContext(), item)
        }
    }

    @Test
    fun shouldCreateNewFactories() {
        val f1: ViewModelFactory = IntBuilder + StringBuilder
        val f2: ViewModelFactory = IntBuilder + StringBuilder
        val f3: ViewModelFactory = IntBuilder + StringBuilder + BooleanBuilder
        val f4: ViewModelFactory = IntBuilder + StringBuilder + BooleanBuilder
        val f5: ViewModelFactory = f1 + BooleanBuilder
        assert(f1 !== f2)
        assert(f1 !== f3)
        assert(f1 !== f4)
        assert(f1 !== f5)
        assert(f2 !== f3)
        assert(f2 !== f4)
        assert(f2 !== f5)
        assert(f3 !== f4)
        assert(f3 !== f5)
        assert(f4 !== f5)
    }

    @Test
    fun verifyComboFactory() {
        val factory: ViewModelFactory = IntBuilder + StringBuilder + BooleanBuilder
        val int1 = factory.onCreateViewModel(context, 1)
        assert(int1 is IntViewModel)
        val str1 = factory.onCreateViewModel(context, "1")
        assert(str1 is StringViewModel)
        val bool1 = factory.onCreateViewModel(context, true)
        assert(bool1 is BooleanViewModel)
    }

}
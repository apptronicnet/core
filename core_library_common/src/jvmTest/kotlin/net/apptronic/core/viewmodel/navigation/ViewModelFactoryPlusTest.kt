package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import org.junit.Test

class ViewModelFactoryPlusTest {

    private val context = createTestContext()

    class IntViewModel(context: Context, val id: Int) : ViewModel(context)
    class StringViewModel(context: Context, val id: String) : ViewModel(context)
    class BooleanViewModel(context: Context, val id: Boolean) : ViewModel(context)

    object IntAdapter : ViewModelAdapter<Int, Int, IntViewModel> {
        override fun getItemId(item: Int): Int = item
        override fun createViewModel(parent: Contextual, item: Int): IntViewModel {
            return IntViewModel(parent.childContext(), item)
        }
    }

    object StringAdapter : ViewModelAdapter<String, String, StringViewModel> {
        override fun getItemId(item: String): String = item
        override fun createViewModel(parent: Contextual, item: String): StringViewModel {
            return StringViewModel(parent.childContext(), item)
        }
    }

    object BooleanAdapter : ViewModelAdapter<Boolean, Boolean, BooleanViewModel> {
        override fun getItemId(item: Boolean): Boolean = item
        override fun createViewModel(parent: Contextual, item: Boolean): BooleanViewModel {
            return BooleanViewModel(parent.childContext(), item)
        }
    }

    @Test
    fun shouldCreateNewFactories() {
        val f1: ViewModelAdapter<Any, Any, IViewModel> = IntAdapter + StringAdapter
        val f2: ViewModelAdapter<Any, Any, IViewModel> = IntAdapter + StringAdapter
        val f3: ViewModelAdapter<Any, Any, IViewModel> = IntAdapter + StringAdapter + BooleanAdapter
        val f4: ViewModelAdapter<Any, Any, IViewModel> = IntAdapter + StringAdapter + BooleanAdapter
        val f5: ViewModelAdapter<Any, Any, IViewModel> = f1 + BooleanAdapter
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
        val factory: ViewModelAdapter<Any, Any, IViewModel> = IntAdapter + StringAdapter + BooleanAdapter
        val int1 = factory.createViewModel(context, 1)
        assert(int1 is IntViewModel)
        val str1 = factory.createViewModel(context, "1")
        assert(str1 is StringViewModel)
        val bool1 = factory.createViewModel(context, true)
        assert(bool1 is BooleanViewModel)
    }

}
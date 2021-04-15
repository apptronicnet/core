package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Contextual
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.viewmodel.TestViewModel
import org.junit.Test

class ViewModelFactoryTest {

    private val context = createTestContext()

    open class BaseType
    open class BaseId
    open class BaseViewModel : TestViewModel()

    class TypeInt(val id: Int, val someValue: String) : BaseType()
    data class IntId(val value: Int) : BaseId()
    class IntViewModel(
            var someValue: String
    ) : BaseViewModel()

    class TypeString(val id: String, val someValue: String) : BaseType()
    data class StringId(val value: String) : BaseId()
    class StringViewModel(
            var someValue: String
    ) : BaseViewModel()

    class IntAdapter : ViewModelAdapter<TypeInt, IntId, IntViewModel> {
        override fun getItemId(item: TypeInt): IntId {
            return IntId(item.id)
        }

        override fun createViewModel(parent: Contextual, item: TypeInt): IntViewModel {
            return IntViewModel(item.someValue)
        }

        override fun updateViewModel(viewModel: IntViewModel, newItem: TypeInt) {
            viewModel.someValue = newItem.someValue
        }

        override fun shouldRetainInstance(item: TypeInt, viewModel: IntViewModel): Boolean {
            return item.id == 1
        }
    }

    class StringAdapter : ViewModelAdapter<TypeString, StringId, StringViewModel> {
        override fun getItemId(item: TypeString): StringId {
            return StringId(item.id)
        }

        override fun createViewModel(parent: Contextual, item: TypeString): StringViewModel {
            return StringViewModel(item.someValue)
        }

        override fun updateViewModel(viewModel: StringViewModel, newItem: TypeString) {
            viewModel.someValue = newItem.someValue
        }

        override fun shouldRetainInstance(item: TypeString, viewModel: StringViewModel): Boolean {
            return item.id == "a"
        }
    }

    @Test
    fun shouldCorrectlyHandleInt() {
        val factory = IntAdapter() + StringAdapter()
        val item1 = TypeInt(1, "Some one 1")
        val item2 = TypeInt(2, "Some one 2")
        assert(factory.getItemId(item1) == IntId(1))
        assert(factory.getItemId(item2) == IntId(2))
        val vm1 = factory.createViewModel(context, item1) as IntViewModel
        val vm2 = factory.createViewModel(context, item2) as IntViewModel
        assert(vm1.someValue == "Some one 1")
        assert(vm2.someValue == "Some one 2")
        factory.updateViewModel(vm1, TypeInt(1, "Some one 3"))
        factory.updateViewModel(vm2, TypeInt(2, "Some one 4"))
        assert(vm1.someValue == "Some one 3")
        assert(vm2.someValue == "Some one 4")
        assert(factory.shouldRetainInstance(item1, vm1))
        assert(factory.shouldRetainInstance(item2, vm2).not())
    }

    @Test
    fun shouldCorrectlyHandleString() {
        val factory = IntAdapter() + StringAdapter()
        val item1 = TypeString("a", "Some one 1")
        val item2 = TypeString("b", "Some one 2")
        assert(factory.getItemId(item1) == StringId("a"))
        assert(factory.getItemId(item2) == StringId("b"))
        val vm1 = factory.createViewModel(context, item1) as StringViewModel
        val vm2 = factory.createViewModel(context, item2) as StringViewModel
        assert(vm1.someValue == "Some one 1")
        assert(vm2.someValue == "Some one 2")
        factory.updateViewModel(vm1, TypeString("a", "Some one 3"))
        factory.updateViewModel(vm2, TypeString("b", "Some one 4"))
        assert(vm1.someValue == "Some one 3")
        assert(vm2.someValue == "Some one 4")
        assert(factory.shouldRetainInstance(item1, vm1))
        assert(factory.shouldRetainInstance(item2, vm2).not())
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldFailOnStringId() {
        val factory = viewModelAdapter {
            +IntAdapter()
        }
        val item1 = TypeString("a", "Some one 1")
        factory.getItemId(item1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldFailOnStringType() {
        val factory = viewModelAdapter {
            +IntAdapter()
        }
        val item1 = TypeString("a", "Some one 1")
        factory.createViewModel(context, item1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldFailOnIntId() {
        val factory = viewModelAdapter {
            +StringAdapter()
        }
        val item1 = TypeInt(1, "Some one 1")
        factory.getItemId(item1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldFailOnIntType() {
        val factory = viewModelAdapter {
            +StringAdapter()
        }
        val item1 = TypeInt(1, "Some one 1")
        factory.createViewModel(context, item1)
    }

}
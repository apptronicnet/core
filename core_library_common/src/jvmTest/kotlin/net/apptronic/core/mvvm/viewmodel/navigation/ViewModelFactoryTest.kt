package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.TestViewModel
import net.apptronic.core.testutils.testContext
import org.junit.Test

class ViewModelFactoryTest {

    private val context = testContext()

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

    class IntBuilder : ViewModelBuilder<TypeInt, IntId, IntViewModel> {
        override fun getId(item: TypeInt): IntId {
            return IntId(item.id)
        }

        override fun onCreateViewModel(parent: Context, item: TypeInt): IntViewModel {
            return IntViewModel(item.someValue)
        }

        override fun onUpdateViewModel(viewModel: IntViewModel, newItem: TypeInt) {
            viewModel.someValue = newItem.someValue
        }

        override fun shouldRetainInstance(item: TypeInt, viewModel: IntViewModel): Boolean {
            return item.id == 1
        }
    }

    class StringBuilder : ViewModelBuilder<TypeString, StringId, StringViewModel> {
        override fun getId(item: TypeString): StringId {
            return StringId(item.id)
        }

        override fun onCreateViewModel(parent: Context, item: TypeString): StringViewModel {
            return StringViewModel(item.someValue)
        }

        override fun onUpdateViewModel(viewModel: StringViewModel, newItem: TypeString) {
            viewModel.someValue = newItem.someValue
        }

        override fun shouldRetainInstance(item: TypeString, viewModel: StringViewModel): Boolean {
            return item.id == "a"
        }
    }

    @Test
    fun shouldCorrectlyHandleInt() {
        val factory = IntBuilder() + StringBuilder()
        val item1 = TypeInt(1, "Some one 1")
        val item2 = TypeInt(2, "Some one 2")
        assert(factory.getId(item1) == IntId(1))
        assert(factory.getId(item2) == IntId(2))
        val vm1 = factory.onCreateViewModel(context, item1) as IntViewModel
        val vm2 = factory.onCreateViewModel(context, item2) as IntViewModel
        assert(vm1.someValue == "Some one 1")
        assert(vm2.someValue == "Some one 2")
        factory.onUpdateViewModel(vm1, TypeInt(1, "Some one 3"))
        factory.onUpdateViewModel(vm2, TypeInt(2, "Some one 4"))
        assert(vm1.someValue == "Some one 3")
        assert(vm2.someValue == "Some one 4")
        assert(factory.shouldRetainInstance(item1, vm1))
        assert(factory.shouldRetainInstance(item2, vm2).not())
    }

    @Test
    fun shouldCorrectlyHandleString() {
        val factory = IntBuilder() + StringBuilder()
        factory.addBuilder(IntBuilder())
        factory.addBuilder(StringBuilder())
        val item1 = TypeString("a", "Some one 1")
        val item2 = TypeString("b", "Some one 2")
        assert(factory.getId(item1) == StringId("a"))
        assert(factory.getId(item2) == StringId("b"))
        val vm1 = factory.onCreateViewModel(context, item1) as StringViewModel
        val vm2 = factory.onCreateViewModel(context, item2) as StringViewModel
        assert(vm1.someValue == "Some one 1")
        assert(vm2.someValue == "Some one 2")
        factory.onUpdateViewModel(vm1, TypeString("a", "Some one 3"))
        factory.onUpdateViewModel(vm2, TypeString("b", "Some one 4"))
        assert(vm1.someValue == "Some one 3")
        assert(vm2.someValue == "Some one 4")
        assert(factory.shouldRetainInstance(item1, vm1))
        assert(factory.shouldRetainInstance(item2, vm2).not())
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldFailOnStringId() {
        val factory = viewModelFactory {
            +IntBuilder()
        }
        val item1 = TypeString("a", "Some one 1")
        factory.getId(item1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldFailOnStringType() {
        val factory = viewModelFactory {
            +IntBuilder()
        }
        val item1 = TypeString("a", "Some one 1")
        factory.onCreateViewModel(context, item1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldFailOnIntId() {
        val factory = viewModelFactory {
            +StringBuilder()
        }
        val item1 = TypeInt(1, "Some one 1")
        factory.getId(item1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldFailOnIntType() {
        val factory = viewModelFactory {
            +StringBuilder()
        }
        val item1 = TypeInt(1, "Some one 1")
        factory.onCreateViewModel(context, item1)
    }

}
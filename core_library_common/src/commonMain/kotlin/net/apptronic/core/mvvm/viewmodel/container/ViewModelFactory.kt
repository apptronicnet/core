package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

fun viewModelFactory(builder: ViewModelFactory<Any, Any?, ViewModel>.() -> Unit): ViewModelFactory<Any, Any?, ViewModel> {
    return ViewModelFactory<Any, Any?, ViewModel>().apply(builder)
}

/**
 * [ViewModelFactory] is implementation of [ViewModelBuilder] which allows to combine several builders instead of using
 * single one for many types. [ViewModelFactory] will choose corresponding builder automatically based on item type. If
 * no [ViewModelBuilder] for type provided - it will throw [IllegalArgumentException]. It allows to combine lists
 * dynamically.
 */
class ViewModelFactory<T : Any, Id, VM : ViewModel> : ViewModelBuilder<T, Id, VM> {

    private val builders = mutableListOf<ViewModelBuilderWrapper<T, Id, VM, out T, out Id, out VM>>()

    inline fun <reified SubT : T, SubId : Id, SubVM : VM> addBuilder(builder: ViewModelBuilder<SubT, SubId, SubVM>) {
        addBuilder(SubT::class, builder)
    }

    fun <SubT : T, SubId : Id, SubVM : VM> addBuilder(clazz: KClass<SubT>, builder: ViewModelBuilder<SubT, SubId, SubVM>) {
        val wrapper = ViewModelBuilderWrapper<T, Id, VM, SubT, SubId, SubVM>(clazz, builder)
        builders.add(wrapper)
    }

    override fun getId(item: T): Id {
        builders.forEach {
            if (it.canUseType(item::class)) {
                return it.getId(item)
            }
        }
        throw IllegalArgumentException("getId for $item failed")
    }

    override fun onCreateViewModel(parent: Context, item: T): VM {
        builders.forEach {
            if (it.canUseType(item::class)) {
                return it.onCreateViewModel(parent, item)
            }
        }
        throw IllegalArgumentException("onCreateViewModel for $item failed")
    }

    override fun onUpdateViewModel(viewModel: VM, newItem: T) {
        builders.forEach {
            if (it.canUseType(newItem::class)) {
                it.onUpdateViewModel(viewModel, newItem)
            }
        }
    }

    override fun shouldRetainInstance(item: T, viewModel: VM): Boolean {
        return builders.any {
            it.canUseType(item::class) && it.shouldRetainInstance(item, viewModel)
        }
    }

    private class ViewModelBuilderWrapper<T : Any, Id, VM : ViewModel, SubT : T, SubId : Id, SubVM : VM>(
            val type: KClass<SubT>,
            val wrapped: ViewModelBuilder<SubT, SubId, SubVM>
    ) : ViewModelBuilder<T, Id, VM> {

        fun canUseType(itemType: KClass<*>): Boolean {
            return itemType == type || itemType.supertypes.any {
                it.classifier != null && it.classifier is KClass<*> && canUseType(it.classifier as KClass<*>)
            }
        }

        override fun getId(item: T): Id {
            return wrapped.getId(item as SubT)
        }

        override fun onCreateViewModel(parent: Context, item: T): VM {
            return wrapped.onCreateViewModel(parent, item as SubT)
        }

        override fun onUpdateViewModel(viewModel: VM, newItem: T) {
            wrapped.onUpdateViewModel(viewModel as SubVM, newItem as SubT)
        }

        override fun shouldRetainInstance(item: T, viewModel: VM): Boolean {
            return wrapped.shouldRetainInstance(item as SubT, viewModel as SubVM)
        }

    }

}
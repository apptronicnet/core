package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.base.utils.isInstanceOf
import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.IViewModel
import kotlin.reflect.KClass

fun viewModelAdapter(builder: CompositeViewModelAdapter.() -> Unit): ViewModelAdapter<Any, Any, IViewModel> {
    return CompositeViewModelAdapter().apply(builder)
}

inline fun <reified T : Any, Id : Any, VM : IViewModel> ViewModelAdapter<T, Id, VM>.asComposite(): ViewModelAdapter<Any, Any, IViewModel> {
    return viewModelAdapter {
        +this@asComposite
    }
}

inline operator fun <reified T1 : Any, reified T2 : Any, Id1 : Any, Id2 : Any, VM1 : IViewModel, VM2 : IViewModel>
        ViewModelAdapter<T1, Id1, VM1>.plus(other: ViewModelAdapter<T2, Id2, VM2>): ViewModelAdapter<Any, Any, IViewModel> {
    val builder = this
    return viewModelAdapter {
        addBuilder(T1::class, builder)
        addBuilder(T2::class, other)
    }
}

/**
 * [CompositeViewModelAdapter] is implementation of [ViewModelAdapter] which allows to combine several builders instead of using
 * single one for many types. [CompositeViewModelAdapter] will choose corresponding builder automatically based on item type. If
 * no [ViewModelAdapter] for type provided - it will throw [IllegalArgumentException]. It allows to combine lists
 * dynamically.
 *
 * Also in wraps all ids into combined ID class, allowing use different [CompositeViewModelAdapter] which can return same ids.
 */
class CompositeViewModelAdapter : ViewModelAdapter<Any, Any, IViewModel> {

    private val builders = mutableListOf<ViewModelAdapterWrapper<*, *, *>>()

    inline fun <reified SubT : Any, SubId : Any, SubVM : IViewModel> addBuilder(adapter: ViewModelAdapter<SubT, SubId, SubVM>) {
        addBuilder(SubT::class, adapter)
    }

    inline operator fun <reified SubT : Any, SubId : Any, SubVM : IViewModel> ViewModelAdapter<SubT, SubId, SubVM>.unaryPlus(): CompositeViewModelAdapter {
        addBuilder(SubT::class, this)
        return this@CompositeViewModelAdapter
    }

    fun <SubT : Any, SubId : Any, SubVM : IViewModel> addBuilder(clazz: KClass<SubT>, adapter: ViewModelAdapter<SubT, SubId, SubVM>) {
        if (adapter is CompositeViewModelAdapter) {
            builders.addAll(adapter.builders)
        } else {
            val wrapper = ViewModelAdapterWrapper(clazz, adapter)
            builders.add(wrapper)
        }
    }

    override fun getItemId(item: Any): Any {
        builders.forEach {
            if (it.isFor(item)) {
                return it.getItemId(item)
            }
        }
        throw IllegalArgumentException("getId for $item failed")
    }

    override fun createViewModel(parent: Contextual, item: Any): IViewModel {
        builders.forEach {
            if (it.isFor(item)) {
                return it.createViewModel(parent, item)
            }
        }
        throw IllegalArgumentException("onCreateViewModel for $item failed")
    }

    override fun updateViewModel(viewModel: IViewModel, newItem: Any) {
        builders.forEach {
            if (it.isFor(newItem)) {
                it.updateViewModel(viewModel, newItem)
            }
        }
    }

    override fun shouldRetainInstance(item: Any, viewModel: IViewModel): Boolean {
        return builders.any {
            it.isFor(item) && it.shouldRetainInstance(item, viewModel)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private class ViewModelAdapterWrapper<SubT : Any, SubId, SubVM : IViewModel>(
            private val type: KClass<SubT>,
            private val wrapped: ViewModelAdapter<SubT, SubId, SubVM>
    ) : ViewModelAdapter<Any, Any, IViewModel> {

        fun isFor(item: Any): Boolean {
            return item isInstanceOf type
        }

        override fun getItemId(item: Any): Any {
            return wrapped.getItemId(item as SubT) as Any
        }

        override fun createViewModel(parent: Contextual, item: Any): IViewModel {
            return wrapped.createViewModel(parent, item as SubT)
        }

        override fun updateViewModel(viewModel: IViewModel, newItem: Any) {
            wrapped.updateViewModel(viewModel as SubVM, newItem as SubT)
        }

        override fun shouldRetainInstance(item: Any, viewModel: IViewModel): Boolean {
            return wrapped.shouldRetainInstance(item as SubT, viewModel as SubVM)
        }

    }

}
package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.utils.isInstanceOf
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

fun viewModelFactory(builder: ViewModelFactory.() -> Unit): ViewModelFactory {
    return ViewModelFactory().apply(builder)
}

inline fun <reified T : Any, Id : Any, VM : ViewModel> ViewModelBuilder<T, Id, VM>.asFactory(): ViewModelFactory {
    return viewModelFactory {
        +this@asFactory
    }
}

inline operator fun <reified T1 : Any, reified T2 : Any, Id1 : Any, Id2 : Any, VM1 : ViewModel, VM2 : ViewModel>
        ViewModelBuilder<T1, Id1, VM1>.plus(other: ViewModelBuilder<T2, Id2, VM2>): ViewModelFactory {
    val builder = this
    return if (this is ViewModelFactory) {
        +other
        this
    } else viewModelFactory {
        +builder
        +other
    }
}

/**
 * [ViewModelFactory] is implementation of [ViewModelBuilder] which allows to combine several builders instead of using
 * single one for many types. [ViewModelFactory] will choose corresponding builder automatically based on item type. If
 * no [ViewModelBuilder] for type provided - it will throw [IllegalArgumentException]. It allows to combine lists
 * dynamically.
 *
 * Also in wraps all ids into combined ID class, allowing use different [ViewModelFactory] which can return same ids.
 */
class ViewModelFactory : ViewModelBuilder<Any, Any, ViewModel> {

    private val builders = mutableListOf<ViewModelBuilderWrapper<*, *, *>>()

    inline fun <reified SubT : Any, SubId : Any, SubVM : ViewModel> addBuilder(builder: ViewModelBuilder<SubT, SubId, SubVM>) {
        addBuilder(SubT::class, builder)
    }

    inline operator fun <reified SubT : Any, SubId : Any, SubVM : ViewModel> ViewModelBuilder<SubT, SubId, SubVM>.unaryPlus(): ViewModelFactory {
        addBuilder(SubT::class, this)
        return this@ViewModelFactory
    }

    fun <SubT : Any, SubId : Any, SubVM : ViewModel> addBuilder(clazz: KClass<SubT>, builder: ViewModelBuilder<SubT, SubId, SubVM>) {
        val wrapper = ViewModelBuilderWrapper(clazz, builder)
        builders.add(wrapper)
    }

    override fun getId(item: Any): Any {
        builders.forEach {
            if (it.isFor(item)) {
                return it.getId(item)
            }
        }
        throw IllegalArgumentException("getId for $item failed")
    }

    override fun onCreateViewModel(parent: Context, item: Any): ViewModel {
        builders.forEach {
            if (it.isFor(item)) {
                return it.onCreateViewModel(parent, item)
            }
        }
        throw IllegalArgumentException("onCreateViewModel for $item failed")
    }

    override fun onUpdateViewModel(viewModel: ViewModel, newItem: Any) {
        builders.forEach {
            if (it.isFor(newItem)) {
                it.onUpdateViewModel(viewModel, newItem)
            }
        }
    }

    override fun shouldRetainInstance(item: Any, viewModel: ViewModel): Boolean {
        return builders.any {
            it.isFor(item) && it.shouldRetainInstance(item, viewModel)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private class ViewModelBuilderWrapper<SubT : Any, SubId, SubVM : ViewModel>(
            private val type: KClass<SubT>,
            private val wrapped: ViewModelBuilder<SubT, SubId, SubVM>
    ) : ViewModelBuilder<Any, Any, ViewModel> {

        fun isFor(item: Any): Boolean {
            return item isInstanceOf type
        }

        override fun getId(item: Any): Any {
            return wrapped.getId(item as SubT) as Any
        }

        override fun onCreateViewModel(parent: Context, item: Any): ViewModel {
            return wrapped.onCreateViewModel(parent, item as SubT)
        }

        override fun onUpdateViewModel(viewModel: ViewModel, newItem: Any) {
            wrapped.onUpdateViewModel(viewModel as SubVM, newItem as SubT)
        }

        override fun shouldRetainInstance(item: Any, viewModel: ViewModel): Boolean {
            return wrapped.shouldRetainInstance(item as SubT, viewModel as SubVM)
        }

    }

}
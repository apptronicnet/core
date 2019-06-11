package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.container.GenericViewModelBuilder

class LazyListBuilder : GenericViewModelBuilder {

    override fun getId(item: Any): Any {
        return when (item) {
            is LazyListItem -> "lazyItem_${item.id}"
            is StaticItem -> "static_${item.id}"
            else -> throw IllegalArgumentException(item.toString())
        }
    }

    override fun onCreateViewModel(parent: Context, item: Any): ViewModel {
        return when (item) {
            is LazyListItem -> LazyListItemViewModel(ViewModelContext(parent))
            is StaticItem -> StaticItemViewModel(ViewModelContext(parent))
            else -> throw IllegalArgumentException(item.toString())
        }.also {
            onUpdateViewModel(it, item)
        }
    }

    override fun onUpdateViewModel(viewModel: ViewModel, newItem: Any) {
        when (newItem) {
            is LazyListItem -> (viewModel as LazyListItemViewModel).item.set(newItem)
            is StaticItem -> (viewModel as StaticItemViewModel).item.set(newItem)
            else -> throw IllegalArgumentException(newItem.toString())
        }
    }

    override fun shouldRetainInstance(list: List<Any>, item: Any, viewModel: ViewModel): Boolean {
        return item is StaticItem
    }

}
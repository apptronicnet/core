package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.GenericViewModelAdapter

class LazyListAdapter : GenericViewModelAdapter {

    override fun getItemId(item: Any): Any {
        return when (item) {
            is LazyListItem -> "lazyItem_${item.id}"
            is StaticItem -> "static_${item.id}"
            else -> throw IllegalArgumentException(item.toString())
        }
    }

    override fun createViewModel(parent: Contextual, item: Any): IViewModel {
        return when (item) {
            is LazyListItem -> parent.lazyListItemViewModel()
            is StaticItem -> parent.staticItemViewModel()
            else -> throw IllegalArgumentException(item.toString())
        }.also {
            updateViewModel(it, item)
        }
    }

    override fun updateViewModel(viewModel: IViewModel, newItem: Any) {
        when (newItem) {
            is LazyListItem -> (viewModel as LazyListItemViewModel).item.set(newItem)
            is StaticItem -> (viewModel as StaticItemViewModel).item.set(newItem)
            else -> throw IllegalArgumentException(newItem.toString())
        }
    }

    override fun shouldRetainInstance(item: Any, viewModel: IViewModel): Boolean {
        return item is StaticItem
    }

}
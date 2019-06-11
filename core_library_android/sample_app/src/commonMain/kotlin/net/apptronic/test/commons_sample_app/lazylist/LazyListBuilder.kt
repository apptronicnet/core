package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.container.ViewModelBuilder

class LazyListBuilder : ViewModelBuilder<LazyListItem, Long, LazyListItemViewModel> {

    override fun getId(item: LazyListItem): Long {
        return item.id
    }

    override fun onCreateViewModel(parent: Context, item: LazyListItem): LazyListItemViewModel {
        return LazyListItemViewModel(ViewModelContext(parent)).also {
            it.item.set(item)
        }
    }

    override fun onUpdateViewModel(viewModel: LazyListItemViewModel, newItem: LazyListItem) {
        viewModel.item.set(newItem)
    }

}
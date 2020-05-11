package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelBuilder

class StaticItem(
    val id: String,
    val delay: Long
)

class DynamicItem(
    val id: Int,
    val text: String
)

class StaticViewModelBuilder : ViewModelBuilder<StaticItem, String, StaticFilteredItemViewModel> {

    override fun getId(item: StaticItem): String {
        return item.id
    }

    override fun onCreateViewModel(parent: Context, item: StaticItem): StaticFilteredItemViewModel {
        return StaticFilteredItemViewModel(parent, item.id, item.delay)
    }

}

class DynamicViewModelBuilder : ViewModelBuilder<DynamicItem, Int, DynamicItemViewModel> {

    override fun getId(item: DynamicItem): Int {
        return item.id
    }

    override fun onCreateViewModel(parent: Context, item: DynamicItem): DynamicItemViewModel {
        return DynamicItemViewModel(parent, item.text)
    }

}
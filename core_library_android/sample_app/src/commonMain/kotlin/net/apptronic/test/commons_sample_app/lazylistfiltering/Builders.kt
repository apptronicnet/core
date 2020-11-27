package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.navigation.ViewModelAdapter

class StaticItem(
    val id: String,
    val delay: Long
)

class DynamicItem(
    val id: Int,
    val text: String
)

object StaticViewModelBuilder : ViewModelAdapter<StaticItem, String, StaticFilteredItemViewModel> {

    override fun getItemId(item: StaticItem): String {
        return item.id
    }

    override fun createViewModel(parent: Context, item: StaticItem): StaticFilteredItemViewModel {
        return StaticFilteredItemViewModel(parent, item.id, item.delay)
    }

}

object DynamicViewModelBuilder : ViewModelAdapter<DynamicItem, Int, DynamicItemViewModel> {

    override fun getItemId(item: DynamicItem): Int {
        return item.id
    }

    override fun createViewModel(parent: Context, item: DynamicItem): DynamicItemViewModel {
        return DynamicItemViewModel(parent, item.text)
    }

}
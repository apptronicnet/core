package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.context.Contextual
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

    override fun createViewModel(
        parent: Contextual,
        item: StaticItem
    ): StaticFilteredItemViewModel {
        return parent.staticFilteredItemViewModel(item.id, item.delay)
    }

}

object DynamicViewModelBuilder : ViewModelAdapter<DynamicItem, Int, DynamicItemViewModel> {

    override fun getItemId(item: DynamicItem): Int {
        return item.id
    }

    override fun createViewModel(parent: Contextual, item: DynamicItem): DynamicItemViewModel {
        return parent.dynamicItemViewModel(item.text)
    }

}
package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.base.collections.wrapLists
import net.apptronic.core.component.entity.behavior.merge
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.hasHidden
import net.apptronic.core.mvvm.viewmodel.navigation.takeWhileVisibleStaticsOnStartFilter
import net.apptronic.core.mvvm.viewmodel.navigation.viewModelFactory

class LazyListFilterViewModel(context: ViewModelContext) : ViewModel(context) {

    private val builder = viewModelFactory {
        addBuilder(StaticViewModelBuilder())
        addBuilder(DynamicViewModelBuilder())
    }

    private val staticItems = value<List<Any>>()
    private val dynamicItems = value<List<DynamicItem>>()
    private val allItems = merge(staticItems, dynamicItems).map {
        wrapLists(it.first, it.second)
    }
    val listNavigator = listRecyclerNavigator(allItems, builder).also {
        it.setStaticItems(staticItems)
    }
    val isInProgress = listNavigator.observerStatus().hasHidden()

    init {
        listNavigator.setSimpleVisibilityFilter()
        listNavigator.setListFilter(takeWhileVisibleStaticsOnStartFilter())
        staticItems.set(
            listOf(
                StaticItem("First", 1000L),
                StaticItem("Second", 2000L),
                StaticItem("Third", 3000L),
                StaticItem("Fourth", 4000L),
                StaticItem("Fifth", 5000L)
            )
        )
        dynamicItems.set((1..200).map {
            DynamicItem(it, "Item #$it")
        })
    }

}
package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.base.collections.wrapLists
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.behavior.merge
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.hasHidden
import net.apptronic.core.mvvm.viewmodel.navigation.listDynamicNavigator
import net.apptronic.core.mvvm.viewmodel.navigation.plus
import net.apptronic.core.mvvm.viewmodel.navigation.takeWhileVisibleStaticsOnStartFilter

class LazyListFilterViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    private val builder = StaticViewModelBuilder + DynamicViewModelBuilder

    private val staticItems = value<List<Any>>()
    private val dynamicItems = value<List<DynamicItem>>()
    private val allItems = merge(staticItems, dynamicItems).map {
        wrapLists(it.first, it.second)
    }
    val listNavigator = listDynamicNavigator(allItems, builder).also {
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
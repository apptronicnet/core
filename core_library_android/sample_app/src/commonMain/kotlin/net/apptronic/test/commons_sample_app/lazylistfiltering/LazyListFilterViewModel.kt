package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.base.collections.wrapLists
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.functions.merge
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.hasHidden
import net.apptronic.core.mvvm.viewmodel.navigation.listDynamicNavigator
import net.apptronic.core.mvvm.viewmodel.navigation.plus
import net.apptronic.core.mvvm.viewmodel.navigation.takeWhileVisibleStaticsOnStartFilter

fun Contextual.lazyListFilterViewModel() = LazyListFilterViewModel(viewModelContext())

class LazyListFilterViewModel internal constructor(context: ViewModelContext) : ViewModel(context) {

    private val builder = StaticViewModelBuilder + DynamicViewModelBuilder

    private val staticItems = value<List<Any>>()
    private val dynamicItems = value<List<DynamicItem>>()
    private val allItems = merge(staticItems, dynamicItems).map {
        wrapLists(it.first, it.second)
    }

    val listNavigator = listDynamicNavigator(allItems, builder).also {
        it.setStaticItems(staticItems)
    }
    val isInProgress = listNavigator.content.hasHidden()

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
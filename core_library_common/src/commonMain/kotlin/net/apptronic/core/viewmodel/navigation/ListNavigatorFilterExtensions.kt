package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel

/**
 * Create simple filter which filters list for only visible items.
 */
fun simpleFilter(): ListNavigatorFilter {
    return SimpleNavigatorFilter()
}

private class SimpleNavigatorFilter : ListNavigatorFilter {
    override fun filterList(source: List<ItemVisibilityRequest>): List<IViewModel> {
        return source.filter { it.isVisible }.map { it.viewModel }
    }
}

/**
 * Create filter which filters list for only not broken chain of visible items from start.
 */
fun takeUntilVisibleFilter(): ListNavigatorFilter {
    return TakeUntilVisibleNavigatorFilter()
}

private class TakeUntilVisibleNavigatorFilter : ListNavigatorFilter {
    override fun filterList(source: List<ItemVisibilityRequest>): List<IViewModel> {
        val index = source.indexOfFirst { it.isVisible.not() }
        return if (index >= 0) {
            source.take(index).map { it.viewModel }
        } else {
            source.map { it.viewModel }
        }
    }
}

/**
 * Create filter which notifies first not visible item each time list changed or visibility on any item changed.
 * [ViewModel] should implement [OnReadyForLoad] or it will be skipped. This allows to implement loading of data
 * inside [ViewModel]s not all at same time but one after another.
 * @param takeUntilVisible defines is filter behavior same as for [takeUntilVisibleFilter]  for true
 * or [simpleFilter ] for false
 * @param countToNotify count of still not visible [ViewModel]s no notify [OnReadyForLoad.setReadyForLoad]
 */
fun notifyNextFilter(takeUntilVisible: Boolean, countToNotify: Int = 1): ListNavigatorFilter {
    return NotifyNextNavigatorFilter(if (takeUntilVisible) takeUntilVisibleFilter() else simpleFilter(), countToNotify)
}

/**
 * Create filter which notifies first not visible item each time list changed or visibility on any item changed.
 * [ViewModel] should implement [OnReadyForLoad] or it will be skipped. This allows to implement loading of data
 * inside [ViewModel]s not all at same time but one after another.
 * @param targetFilter defines filter which be used for filtering itself
 * @param countToNotify count of still not visible [ViewModel]s no notify [OnReadyForLoad.setReadyForLoad]
 */
fun notifyNextFilter(targetFilter: ListNavigatorFilter, countToNotify: Int = 1): ListNavigatorFilter {
    return NotifyNextNavigatorFilter(targetFilter, countToNotify)
}

private class NotifyNextNavigatorFilter(
        private val targetFilter: ListNavigatorFilter,
        private val countToNotify: Int
) : ListNavigatorFilter {
    override fun filterList(source: List<ItemVisibilityRequest>): List<IViewModel> {
        source.filter { it.isVisible.not() && it.viewModel is OnReadyForLoad }
                .take(countToNotify)
                .forEach {
                    (it.viewModel as? OnReadyForLoad)?.setReadyForLoad()
                }
        return targetFilter.filterList(source)
    }
}

/**
 * Interface for use with [notifyNextFilter]
 */
interface OnReadyForLoad {

    /**
     * Called when [notifyNextFilter] marks [ViewModel] which implements this interface that it can load data.
     */
    fun setReadyForLoad()

}
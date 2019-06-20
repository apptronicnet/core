package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Create simple filter which filters list for only visible items
 */
fun simpleFilter(): ViewModelListFilter {
    return SimpleFilter()
}

private class SimpleFilter : ViewModelListFilter {
    override fun filterList(source: List<ViewModelVisibilityRequest>): List<ViewModel> {
        return source.filter { it.isVisible }.map { it.viewModel }
    }
}

/**
 * Create filter which filters list for only not broken chain of visible items from start
 */
fun takeUntilVisibleFilter(): ViewModelListFilter {
    return TakeUntilVisibleFilter()
}

private class TakeUntilVisibleFilter : ViewModelListFilter {
    override fun filterList(source: List<ViewModelVisibilityRequest>): List<ViewModel> {
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
fun notifyNextFilter(takeUntilVisible: Boolean, countToNotify: Int = 1): ViewModelListFilter {
    return NotifyNextFilter(if (takeUntilVisible) takeUntilVisibleFilter() else simpleFilter(), countToNotify)
}

/**
 * Create filter which notifies first not visible item each time list changed or visibility on any item changed.
 * [ViewModel] should implement [OnReadyForLoad] or it will be skipped. This allows to implement loading of data
 * inside [ViewModel]s not all at same time but one after another.
 * @param targetFilter defines filter which be used for filtering itself
 * @param countToNotify count of still not visible [ViewModel]s no notify [OnReadyForLoad.setReadyForLoad]
 */
fun notifyNextFilter(targetFilter: ViewModelListFilter, countToNotify: Int = 1): ViewModelListFilter {
    return NotifyNextFilter(targetFilter, countToNotify)
}

private class NotifyNextFilter(
        private val targetFilter: ViewModelListFilter,
        private val countToNotify: Int
) : ViewModelListFilter {
    override fun filterList(source: List<ViewModelVisibilityRequest>): List<ViewModel> {
        source.filter { it.isVisible.not() && it.viewModel is OnReadyForLoad }
                .take(countToNotify)
                .forEach {
                    (it.viewModel as? OnReadyForLoad)?.setReadyForLoad()
                }
        return targetFilter.filterList(source)
    }
}

interface OnReadyForLoad {

    fun setReadyForLoad()

}
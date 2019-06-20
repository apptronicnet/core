package net.apptronic.test.commons_sample_app.loadfilterlist

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.container.addVisibilityFilter
import net.apptronic.core.mvvm.viewmodel.container.notifyNextFilter
import net.apptronic.core.mvvm.viewmodel.container.simpleFilter
import net.apptronic.core.mvvm.viewmodel.container.takeUntilVisibleFilter

fun createLoadFilterListViewModel(parent: Context): LoadFilterListViewModel {
    return LoadFilterListViewModel(ViewModelContext(parent))
}

class LoadFilterListViewModel(context: ViewModelContext) : ViewModel(context) {

    val loadFilterMode = value<LoadFilterMode>(LoadFilterMode.Simple)

    val list = listNavigator()

    init {
        list.addVisibilityFilter(LoadItemVisibilityFilter())
        loadFilterMode.map { mode ->
            when (mode) {
                LoadFilterMode.Simple -> {
                    val times = (1..40).map { it * 500L }
                    list.set(times.map { createLoadItemViewModel(this, it, true) })
                    list.setListFilter(simpleFilter())
                }
                LoadFilterMode.Random -> {
                    val times = (1..40).map { it * 500L }.shuffled()
                    list.set(times.map { createLoadItemViewModel(this, it, true) })
                    list.setListFilter(simpleFilter())
                }
                LoadFilterMode.RandomTakeFirst -> {
                    val times = (1..20).map { it * 200L }.shuffled()
                    list.set(times.map { createLoadItemViewModel(this, it, true) })
                    list.setListFilter(takeUntilVisibleFilter())
                }
                LoadFilterMode.RandomWithNotifyReady -> {
                    val times = (1..40).map { 250L + it * 12L }.shuffled()
                    list.set(times.map { createLoadItemViewModel(this, it, false) })
                    list.setListFilter(notifyNextFilter(true, 3))
                }
            }
        }
    }

}
package net.apptronic.test.commons_sample_app.loadfilterlist

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.*

fun Contextual.loadFilterListViewModel() = LoadFilterListViewModel(childContext())

class LoadFilterListViewModel internal constructor(context: Context) : ViewModel(context) {

    val loadFilterMode = value<LoadFilterMode>(LoadFilterMode.Simple)

    val list = listNavigator()

    private fun List<Long>.items(initialReady: Boolean): List<LoadItemViewModel> {
        return mapIndexed { index, time ->
            loadItemViewModel(index + 1, time, initialReady)
        }
    }

    init {
        list.addVisibilityFilter(LoadItemVisibilityFilter())
        loadFilterMode.map { mode ->
            when (mode) {
                LoadFilterMode.Simple -> {
                    val times = (1..40).map { it * 300L }
                    list.set(times.items(true))
                    list.setListFilter(simpleFilter())
                }
                LoadFilterMode.Random -> {
                    val times = (1..40).map { it * 150L }.shuffled()
                    list.set(times.items(true))
                    list.setListFilter(simpleFilter())
                }
                LoadFilterMode.RandomTakeFirst -> {
                    val times = (1..20).map { it * 150L }.shuffled()
                    list.set(times.items(true))
                    list.setListFilter(takeUntilVisibleFilter())
                }
                LoadFilterMode.RandomWithNotifyReady -> {
                    val times = (1..40).map { 150L + it * 8L }.shuffled()
                    list.set(times.items(false))
                    list.setListFilter(notifyNextFilter(true, 3))
                }
            }
        }
    }

}
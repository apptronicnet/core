package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.threading.execute

abstract class BaseListNavigator<T>(
    parent: ViewModel
) : Navigator<List<T>>(parent) {

    abstract fun setAdapter(adapter: ViewModelListAdapter)

    private var postRefreshingVisibility = false

    internal fun postRefreshVisibility() {
        if (!postRefreshingVisibility) {
            postRefreshingVisibility = true
            uiAsyncWorker.execute {
                refreshVisibility()
                postRefreshingVisibility = false
            }
        }
    }

    internal abstract fun refreshVisibility()


}
package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

abstract class BaseListNavigator<T>(
        private val parent: ViewModel
) : Navigator<List<T>>(parent) {

    private var adapter: ViewModelListAdapter? = null

    private var postRefreshingVisibility = false
    private var postRefreshingAdapter = false

    fun setAdapter(adapter: ViewModelListAdapter) {
        this.adapter = adapter
        onSetAdapter(adapter)
        onNotifyAdapter(adapter)
        parent.getLifecycle().onExitFromActiveStage {
            this.adapter = null
        }
    }

    protected abstract fun onSetAdapter(adapter: ViewModelListAdapter)

    fun notifyAdapter() {
        if (!postRefreshingAdapter) {
            postRefreshingAdapter = true
            coroutineLauncher.launch {
                val adapter = this@BaseListNavigator.adapter
                if (adapter != null) {
                    onNotifyAdapter(adapter)
                }
                postRefreshingAdapter = false
            }
        }
    }

    protected abstract fun onNotifyAdapter(adapter: ViewModelListAdapter)

    internal fun postRefreshVisibility() {
        if (!postRefreshingVisibility) {
            postRefreshingVisibility = true
            coroutineLauncher.launch {
                refreshVisibility()
                postRefreshingVisibility = false
            }
        }
    }

    internal abstract fun refreshVisibility()

}
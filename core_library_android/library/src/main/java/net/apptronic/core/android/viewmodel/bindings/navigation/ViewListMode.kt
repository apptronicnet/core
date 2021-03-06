package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.listadapters.BindingStrategy
import net.apptronic.core.android.viewmodel.listadapters.RecyclerViewAdapter
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.android.viewmodel.style.list.ViewStyleAdapter
import net.apptronic.core.android.viewmodel.style.list.emptyViewStyleAdapter
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.models.SupportsViewModelListAdapter

fun BindingContainer.bindNavigator(
    container: RecyclerView,
    navigator: SupportsViewModelListAdapter<*>,
    mode: ViewListMode
) {
    +ViewListModeBinding(container, navigator, mode)
}

private class ViewListModeBinding(
    private val container: RecyclerView,
    private val navigator: SupportsViewModelListAdapter<*>,
    private val mode: ViewListMode
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val viewModelAdapter =
            ViewBinderListAdapter(
                container,
                getComposedViewBinderAdapter(mode.binderAdapter, viewModel),
                mode.styleAdapter ?: emptyViewStyleAdapter()
            )
        val adapter = RecyclerViewAdapter(viewModelAdapter, mode.bindingStrategy)
        container.adapter = adapter
        navigator.setAdapter(viewModelAdapter)
        onUnbind {
            container.stopScroll()
            container.stopNestedScroll()
            adapter.onUnbound()
        }
    }

}

class ViewListMode(
    val binderAdapter: ViewBinderAdapter? = null,
    val styleAdapter: ViewStyleAdapter? = null,
    val bindingStrategy: BindingStrategy = BindingStrategy.MatchRecycle
)
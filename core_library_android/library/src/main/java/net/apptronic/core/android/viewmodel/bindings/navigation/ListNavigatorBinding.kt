package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewFactory
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.listadapters.BindingStrategy
import net.apptronic.core.android.viewmodel.listadapters.RecyclerViewAdapter
import net.apptronic.core.android.viewmodel.style.list.ListItemStyleAdapter
import net.apptronic.core.android.viewmodel.style.list.emptyStyleAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.BaseListNavigator

fun listNavigatorBinding(
    recyclerView: RecyclerView,
    navigator: BaseListNavigator<*>,
    factory: AndroidViewFactory,
    styleAdapter: ListItemStyleAdapter = emptyStyleAdapter(),
    bindingStrategy: BindingStrategy = BindingStrategy.MatchRecycle
): ListNavigatorBinding {
    return ListNavigatorBinding(recyclerView, navigator, factory, styleAdapter, bindingStrategy)
}

class ListNavigatorBinding(
    private val recyclerView: RecyclerView,
    private val navigator: BaseListNavigator<*>,
    private val factory: AndroidViewFactory,
    private val styleAdapter: ListItemStyleAdapter,
    private val bindingStrategy: BindingStrategy
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        val viewModelAdapter = AndroidViewModelListAdapter(factory, styleAdapter)
        recyclerView.adapter = RecyclerViewAdapter(viewModelAdapter, bindingStrategy)
        navigator.setAdapter(viewModelAdapter)
    }

}
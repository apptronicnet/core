package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewFactory
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.listadapters.RecyclerViewAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.container.ViewModelListNavigator

fun listNavigatorBinding(
    recyclerView: RecyclerView,
    navigator: ViewModelListNavigator,
    factory: AndroidViewFactory
): ListNavigatorBinding {
    return ListNavigatorBinding(recyclerView, navigator, factory)
}

class ListNavigatorBinding(
    private val recyclerView: RecyclerView,
    private val navigator: ViewModelListNavigator,
    private val factory: AndroidViewFactory
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        val viewModelAdapter = AndroidViewModelListAdapter(factory)
        recyclerView.adapter = RecyclerViewAdapter(viewModelAdapter)
        navigator.setAdapter(viewModelAdapter)
    }

}
package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.plugins.getAndroidViewFactoryFromExtension
import net.apptronic.core.android.viewmodel.*
import net.apptronic.core.android.viewmodel.listadapters.BindingStrategy
import net.apptronic.core.android.viewmodel.listadapters.RecyclerViewAdapter
import net.apptronic.core.android.viewmodel.style.list.ListItemStyleAdapter
import net.apptronic.core.android.viewmodel.style.list.emptyStyleAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.BaseListNavigator

fun BindingContainer.bindListNavigator(
    recyclerView: RecyclerView,
    navigator: BaseListNavigator<*>,
    factory: AndroidViewFactory? = null,
    styleAdapter: ListItemStyleAdapter = emptyStyleAdapter(),
    bindingStrategy: BindingStrategy = BindingStrategy.MatchRecycle
) {
    val resultFactory = factory
        ?: navigator.parent.getAndroidViewFactoryFromExtension()
        ?: throw IllegalArgumentException("AndroidViewFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    add(ListNavigatorBinding(recyclerView, navigator, resultFactory, styleAdapter, bindingStrategy))
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
        val adapter = RecyclerViewAdapter(viewModelAdapter, bindingStrategy)
        recyclerView.adapter = adapter
        navigator.setAdapter(viewModelAdapter)
        onUnbind {
            recyclerView.stopScroll()
            recyclerView.stopNestedScroll()
            adapter.onUnbound()
        }
    }

}
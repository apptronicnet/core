package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.listadapters.BindingStrategy
import net.apptronic.core.android.viewmodel.listadapters.RecyclerViewAdapter
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.android.viewmodel.style.list.ListItemStyleAdapter
import net.apptronic.core.android.viewmodel.style.list.emptyStyleAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.BaseListNavigator

fun BindingContainer.bindListNavigator(
    recyclerView: RecyclerView,
    navigator: BaseListNavigator<*, *>,
    factory: ViewBinderFactory? = null,
    styleAdapter: ListItemStyleAdapter = emptyStyleAdapter(),
    bindingStrategy: BindingStrategy = BindingStrategy.MatchRecycle
) {
    val resultFactory = factory
        ?: navigator.parent.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("ViewBinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    +ListNavigatorBinding(recyclerView, navigator, resultFactory, styleAdapter, bindingStrategy)
}

class ListNavigatorBinding(
    private val recyclerView: RecyclerView,
    private val navigator: BaseListNavigator<*, *>,
    private val factory: ViewBinderFactory,
    private val styleAdapter: ListItemStyleAdapter,
    private val bindingStrategy: BindingStrategy
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val viewModelAdapter =
            ViewBinderListAdapter(
                factory,
                styleAdapter
            )
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
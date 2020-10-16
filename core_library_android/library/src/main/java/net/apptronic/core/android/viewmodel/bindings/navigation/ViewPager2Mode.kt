package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.viewpager2.widget.ViewPager2
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.listadapters.BindingStrategy
import net.apptronic.core.android.viewmodel.listadapters.RecyclerViewAdapter
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.android.viewmodel.style.list.ViewStyleAdapter
import net.apptronic.core.android.viewmodel.style.list.emptyViewStyleAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StaticListNavigator

fun BindingContainer.bindNavigator(
    container: ViewPager2,
    navigator: StaticListNavigator<*>,
    mode: ViewPager2Mode
) {
    +ViewPager2NavigatorBinding(container, navigator, mode)
}

private class ViewPager2NavigatorBinding(
    private val container: ViewPager2,
    private val navigator: StaticListNavigator<*>,
    private val mode: ViewPager2Mode
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val binderFactory = getComposedViewBinderFactory(mode.binderFactory, viewModel)
        val styleAdapter = mode.styleAdapter ?: emptyViewStyleAdapter()
        val viewModelAdapter = ViewBinderListAdapter(binderFactory, styleAdapter)
        val adapter = RecyclerViewAdapter(viewModelAdapter, mode.bindingStrategy)
        container.adapter = adapter
        navigator.setAdapter(viewModelAdapter)
        onUnbind {
            container.stopNestedScroll()
            adapter.onUnbound()
        }
    }

}

class ViewPager2Mode(
    val binderFactory: ViewBinderFactory? = null,
    val styleAdapter: ViewStyleAdapter? = null,
    val bindingStrategy: BindingStrategy = BindingStrategy.MatchRecycle
)
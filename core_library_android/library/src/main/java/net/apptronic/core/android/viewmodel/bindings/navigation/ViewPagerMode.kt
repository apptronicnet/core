package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.viewpager.widget.ViewPager
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.listadapters.TitleAdapter
import net.apptronic.core.android.viewmodel.listadapters.TitleProvider
import net.apptronic.core.android.viewmodel.listadapters.ViewPagerAdapter
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.android.viewmodel.style.list.ViewStyleAdapter
import net.apptronic.core.android.viewmodel.style.list.emptyViewStyleAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StaticListNavigator

fun BindingContainer.bindNavigator(
    container: ViewPager,
    navigator: StaticListNavigator<*>,
    mode: ViewPagerMode
) {
    +ViewPagerNavigatorBinding(container, navigator, mode)
}

private class ViewPagerNavigatorBinding(
    private val container: ViewPager,
    private val navigator: StaticListNavigator<*>,
    private val mode: ViewPagerMode
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val viewModelAdapter = ViewBinderListAdapter(
            container,
            getComposedViewBinderFactory(mode.binderFactory, viewModel),
            mode.styleAdapter ?: emptyViewStyleAdapter()
        )
        container.adapter = ViewPagerAdapter(viewModelAdapter).apply {
            if (mode.titleAdapter != null) {
                titleProvider = TitleProvider(container.context, mode.titleAdapter)
            }
        }
        navigator.setAdapter(viewModelAdapter)
    }

}

class ViewPagerMode(
    val binderFactory: ViewBinderFactory? = null,
    val titleAdapter: TitleAdapter? = null,
    val styleAdapter: ViewStyleAdapter? = null
)
package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.viewpager.widget.ViewPager
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewFactory
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.listadapters.ViewPagerAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.container.ViewModelListNavigator

fun pagerNavigatorBinding(
    viewPager: ViewPager,
    navigator: ViewModelListNavigator,
    factory: AndroidViewFactory
): PagerNavigatorBinding {
    return PagerNavigatorBinding(viewPager, navigator, factory)
}

class PagerNavigatorBinding(
    private val viewPager: ViewPager,
    private val navigator: ViewModelListNavigator,
    private val factory: AndroidViewFactory
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        val viewModelAdapter = AndroidViewModelListAdapter(factory)
        viewPager.adapter = ViewPagerAdapter(viewModelAdapter)
        navigator.setAdapter(viewModelAdapter)
    }

}
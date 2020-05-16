package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.viewpager.widget.ViewPager
import net.apptronic.core.android.plugins.getAndroidViewFactoryFromExtension
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewFactory
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.listadapters.TitleFactory
import net.apptronic.core.android.viewmodel.listadapters.TitleProvider
import net.apptronic.core.android.viewmodel.listadapters.ViewPagerAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ListNavigator

fun AndroidView<*>.pagerNavigatorBinding(
    viewPager: ViewPager,
    navigator: ListNavigator,
    factory: AndroidViewFactory? = null,
    titleFactory: TitleFactory? = null
) {
    val resultFactory = factory
        ?: navigator.parent.getAndroidViewFactoryFromExtension()
        ?: throw IllegalArgumentException("AndroidViewFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    add(PagerNavigatorBinding(viewPager, navigator, resultFactory, titleFactory))
}

private class PagerNavigatorBinding(
    private val viewPager: ViewPager,
    private val navigator: ListNavigator,
    private val factory: AndroidViewFactory,
    private val titleFactory: TitleFactory?
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        val viewModelAdapter = AndroidViewModelListAdapter(factory)
        viewPager.adapter = ViewPagerAdapter(viewModelAdapter).apply {
            if (titleFactory != null) {
                titleProvider = TitleProvider(viewPager.context, titleFactory)
            }
        }
        navigator.setAdapter(viewModelAdapter)
    }

}
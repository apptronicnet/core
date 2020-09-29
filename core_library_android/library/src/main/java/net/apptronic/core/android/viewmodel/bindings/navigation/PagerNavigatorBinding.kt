package net.apptronic.core.android.viewmodel.bindings.navigation

import androidx.viewpager.widget.ViewPager
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.listadapters.TitleFactory
import net.apptronic.core.android.viewmodel.listadapters.TitleProvider
import net.apptronic.core.android.viewmodel.listadapters.ViewPagerAdapter
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ListNavigator

fun BindingContainer.bindPagerNavigator(
    viewPager: ViewPager,
    navigator: ListNavigator,
    factory: ViewBinderFactory? = null,
    titleFactory: TitleFactory? = null
) {
    val resultFactory = factory
        ?: navigator.parent.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("ViewBinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    +PagerNavigatorBinding(viewPager, navigator, resultFactory, titleFactory)
}

private class PagerNavigatorBinding(
    private val viewPager: ViewPager,
    private val navigator: ListNavigator,
    private val factory: ViewBinderFactory,
    private val titleFactory: TitleFactory?
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val viewModelAdapter =
            ViewBinderListAdapter(
                factory,
                itemStateNavigator = navigator
            )
        viewPager.adapter = ViewPagerAdapter(viewModelAdapter).apply {
            if (titleFactory != null) {
                titleProvider = TitleProvider(viewPager.context, titleFactory)
            }
        }
        navigator.setAdapter(viewModelAdapter)
    }

}
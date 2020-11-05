package net.apptronic.test.commons_sample_app.binders.tabs

import android.view.View
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.core.entity.function.map
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.TabsBinding
import net.apptronic.test.commons_sample_app.tabs.TabsViewModel

class TabsViewBinder : ViewBinder<TabsViewModel>() {

    override var layoutResId: Int? = R.layout.tabs

    private val viewBinding by viewBinding(TabsBinding::bind)

    override fun onBindView() {
        with(viewBinding) {
            bindNavigator(tabContainer, viewModel.tabNavigator)
            bindTab(tab1, viewModel, 0)
            bindTab(tab2, viewModel, 1)
            bindTab(tab3, viewModel, 2)
            bindTab(tab4, viewModel, 3)
        }
    }

    private fun bindTab(view: View, viewModel: TabsViewModel, index: Int) {
        viewModel.tabNavigator.content.map {
            it.visibleIndex == index
        }.subscribe {
            view.isActivated = it
        }
        view.setOnClickListener {
            viewModel.selectTab(index)
        }
    }

}
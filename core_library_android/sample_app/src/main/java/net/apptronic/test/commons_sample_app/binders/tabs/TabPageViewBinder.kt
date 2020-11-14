package net.apptronic.test.commons_sample_app.binders.tabs

import androidx.recyclerview.widget.LinearLayoutManager
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.TabPageBinding
import net.apptronic.test.commons_sample_app.tabs.TabPageViewModel

class TabPageViewBinder : ViewBinder<TabPageViewModel>() {

    override var layoutResId: Int? = R.layout.tab_page

    private val viewBinding by viewBinding(TabPageBinding::bind)

    override fun onBindView() {
        with(viewBinding) {
            bindText(tabTitle, viewModel.title)
            tabItemsList.layoutManager = LinearLayoutManager(context)
            bindNavigator(tabItemsList, viewModel.listNavigator)
            tabItemsList.setBackgroundColor(viewModel.backgroundColor)
        }
    }

}
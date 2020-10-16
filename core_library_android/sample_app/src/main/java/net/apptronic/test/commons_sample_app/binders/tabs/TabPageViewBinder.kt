package net.apptronic.test.commons_sample_app.binders.tabs

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.tab_page.view.*
import net.apptronic.core.android.view.platform.colorInt
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.tabs.TabPageViewModel

class TabPageViewBinder : ViewBinder<TabPageViewModel>() {

    override var layoutResId: Int? = R.layout.tab_page

    override fun onBindView(view: View, viewModel: TabPageViewModel) {
        with(view) {
            bindText(tabTitle, viewModel.title)
            tabItemsList.layoutManager = LinearLayoutManager(context)
            bindNavigator(tabItemsList, viewModel.listNavigator)
            tabItemsList.setBackgroundColor(viewModel.backgroundColor.colorInt)
        }
    }

}
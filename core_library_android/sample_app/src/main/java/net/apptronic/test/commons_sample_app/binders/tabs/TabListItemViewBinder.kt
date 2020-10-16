package net.apptronic.test.commons_sample_app.binders.tabs

import android.view.View
import kotlinx.android.synthetic.main.tab_list_item.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.tabs.TabListItemViewModel

class TabListItemViewBinder : ViewBinder<TabListItemViewModel>() {

    override var layoutResId: Int? = R.layout.tab_list_item

    override fun onBindView(view: View, viewModel: TabListItemViewModel) {
        bindText(view.text, viewModel.text)
    }

}
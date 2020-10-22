package net.apptronic.test.commons_sample_app.binders.tabs

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.TabListItemBinding
import net.apptronic.test.commons_sample_app.tabs.TabListItemViewModel

class TabListItemViewBinder : ViewBinder<TabListItemViewModel>() {

    override var layoutResId: Int? = R.layout.tab_list_item

    private val viewBinding by viewBinding(TabListItemBinding::bind)

    override fun onBindView() = with(viewBinding) {
        bindText(text, viewModel.text)
    }

}
package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.LoadFilterItemBinding
import net.apptronic.test.commons_sample_app.loadfilterlist.LoadItemViewModel

class LoadFilterListItemViewBinder : ViewBinder<LoadItemViewModel>() {

    override var layoutResId: Int? = R.layout.load_filter_item

    override fun onBindView() = withBinging(LoadFilterItemBinding::bind) {
        bindText(loadFilterItemText, viewModel.text)
    }

}
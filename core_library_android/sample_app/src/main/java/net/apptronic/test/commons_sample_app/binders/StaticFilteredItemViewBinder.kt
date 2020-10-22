package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.SimpleTextItemBinding
import net.apptronic.test.commons_sample_app.lazylistfiltering.StaticFilteredItemViewModel

class StaticFilteredItemViewBinder : ViewBinder<StaticFilteredItemViewModel>() {

    override var layoutResId: Int? = R.layout.simple_text_item

    override fun onBindView() = withBinging(SimpleTextItemBinding::bind) {
        bindText(simpleText, viewModel.text)
    }

}
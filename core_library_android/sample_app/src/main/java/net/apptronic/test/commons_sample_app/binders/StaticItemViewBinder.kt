package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.StaticListItemBinding
import net.apptronic.test.commons_sample_app.lazylist.StaticItemViewModel

class StaticItemViewBinder : ViewBinder<StaticItemViewModel>() {

    override var layoutResId: Int? = R.layout.static_list_item

    private val viewBinding by viewBinding(StaticListItemBinding::bind)

    override fun onBindView() = with(viewBinding) {
        bindText(text, viewModel.text)
    }

}
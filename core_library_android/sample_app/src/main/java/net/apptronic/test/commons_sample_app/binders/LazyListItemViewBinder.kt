package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.LazyListItemBinding
import net.apptronic.test.commons_sample_app.lazylist.LazyListItemViewModel

class LazyListItemViewBinder : ViewBinder<LazyListItemViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_list_item

    override fun onBindView() = withBinding(LazyListItemBinding::bind) {
        bindText(number, viewModel.number)
        bindText(text, viewModel.text)
        bindClickListener(lazyListItem, viewModel.onClick)
    }

}
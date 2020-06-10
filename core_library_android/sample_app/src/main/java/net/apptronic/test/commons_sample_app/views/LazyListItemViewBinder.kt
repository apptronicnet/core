package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.lazy_list_item.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylist.LazyListItemViewModel

class LazyListItemViewBinder : ViewBinder<LazyListItemViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_list_item

    override fun onBindView(view: View, viewModel: LazyListItemViewModel) {
        with(view) {
            bindText(number, viewModel.number)
            bindText(text, viewModel.text)
            bindClickListener(lazyListItem, viewModel.onClick)
        }
    }

}
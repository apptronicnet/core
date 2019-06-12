package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.lazy_list_item.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylist.LazyListItemViewModel

class LazyListItemView : AndroidView<LazyListItemViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_list_item

    override fun onBindView(view: View, viewModel: LazyListItemViewModel) {
        with(view) {
            +(number setTextFrom viewModel.number)
            +(text setTextFrom viewModel.text)
            +(lazyListItem sendClicksTo viewModel.onClick)
        }
    }

}
package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.static_list_item.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylist.StaticItemViewModel

class StaticItemView : AndroidView<StaticItemViewModel>() {

    override var layoutResId: Int? = R.layout.static_list_item

    override fun onBindView(view: View, viewModel: StaticItemViewModel) {
        with(view) {
            +(text setTextFrom viewModel.text)
        }
    }

}
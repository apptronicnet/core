package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.load_filter_item.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.loadfilterlist.LoadItemViewModel

class LoadFilterListItemView : AndroidView<LoadItemViewModel>() {

    override var layoutResId: Int? = R.layout.load_filter_item

    override fun onBindView(view: View, viewModel: LoadItemViewModel) {
        with(view) {
            +(loadFilterItemText setTextFrom viewModel.text)
        }
    }

}
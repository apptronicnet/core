package net.apptronic.test.commons_sample_app.binders

import android.view.View
import kotlinx.android.synthetic.main.static_list_item.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylist.StaticItemViewModel

class StaticItemViewBinder : ViewBinder<StaticItemViewModel>() {

    override var layoutResId: Int? = R.layout.static_list_item

    override fun onBindView(view: View, viewModel: StaticItemViewModel) {
        with(view) {
            bindText(text, viewModel.text)
        }
    }

}
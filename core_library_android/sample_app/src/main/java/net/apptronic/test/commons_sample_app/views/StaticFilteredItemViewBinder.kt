package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.simple_text_item.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylistfiltering.StaticFilteredItemViewModel

class StaticFilteredItemViewBinder : ViewBinder<StaticFilteredItemViewModel>() {

    override var layoutResId: Int? = R.layout.simple_text_item

    override fun onBindView(view: View, viewModel: StaticFilteredItemViewModel) {
        with(view) {
            bindText(simpleText, viewModel.text)
        }
    }

}
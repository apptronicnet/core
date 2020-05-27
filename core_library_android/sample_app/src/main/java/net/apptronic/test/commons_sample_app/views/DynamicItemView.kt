package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.simple_text_item.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylistfiltering.DynamicItemViewModel

class DynamicItemView : AndroidView<DynamicItemViewModel>() {

    override var layoutResId: Int? = R.layout.simple_text_item

    override fun onBindView(view: View, viewModel: DynamicItemViewModel) {
        with(view) {
            bindText(simpleText, viewModel.text)
        }
    }


}
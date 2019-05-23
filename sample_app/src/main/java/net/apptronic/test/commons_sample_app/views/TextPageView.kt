package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.page_text.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.pager.pages.TextPageViewModel

class TextPageView : AndroidView<TextPageViewModel>() {

    init {
        layoutResId = R.layout.page_text
    }

    override fun onBindView(view: View, viewModel: TextPageViewModel) {
        with(view) {
            +(textPageNumber setTextFrom viewModel.number)
            +(textPageValue setTextFrom viewModel.text)
            +(textPageValue sendClicksTo viewModel::onTextClick)
        }
    }

}
package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.page_text.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.pager.pages.TextPageViewModel

class TextPageViewBinder : ViewBinder<TextPageViewModel>() {

    override var layoutResId: Int? = R.layout.page_text

    override fun onBindView(view: View, viewModel: TextPageViewModel) {
        with(view) {
            bindText(textPageNumber, viewModel.number)
            bindText(textPageValue, viewModel.text)
            bindClickListener(textPageValue, viewModel::onTextClick)
        }
    }

}
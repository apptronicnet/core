package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.PageTextBinding
import net.apptronic.test.commons_sample_app.pager.pages.TextPageViewModel

class TextPageViewBinder : ViewBinder<TextPageViewModel>() {

    override var layoutResId: Int? = R.layout.page_text

    override fun onBindView() = withBinding(PageTextBinding::bind) {
        bindText(textPageNumber, viewModel.number)
        bindText(textPageValue, viewModel.text)
        bindClickListener(textPageValue, viewModel::onTextClick)
    }

}
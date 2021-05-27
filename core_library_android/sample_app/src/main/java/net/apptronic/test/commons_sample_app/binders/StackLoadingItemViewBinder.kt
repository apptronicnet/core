package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.StackLoadingItemBinding
import net.apptronic.test.commons_sample_app.stackloading.StackItemViewModel

class StackLoadingItemViewBinder : ViewBinder<StackItemViewModel>() {

    override var layoutResId: Int? = R.layout.stack_loading_item

    override fun onBindView() = withBinding(StackLoadingItemBinding::bind) {
        bindText(text, viewModel.text)
        bindClickListener(navigatorAdd, viewModel.onClickAdd)
        bindClickListener(navigatorRepalce, viewModel.onClickReplace)
        bindClickListener(navigatorRepalceAll, viewModel.onClickReplaceAll)
    }

}
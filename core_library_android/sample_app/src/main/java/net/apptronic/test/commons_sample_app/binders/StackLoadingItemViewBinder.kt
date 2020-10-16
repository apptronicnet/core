package net.apptronic.test.commons_sample_app.binders

import android.view.View
import kotlinx.android.synthetic.main.stack_loading_item.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.stackloading.StackItemViewModel

class StackLoadingItemViewBinder : ViewBinder<StackItemViewModel>() {

    override var layoutResId: Int? = R.layout.stack_loading_item

    override fun onBindView(view: View, viewModel: StackItemViewModel) {
        with(view) {
            bindText(text, viewModel.text)
            bindClickListener(navigatorAdd, viewModel.onClickAdd)
            bindClickListener(navigatorRepalce, viewModel.onClickReplace)
            bindClickListener(navigatorRepalceAll, viewModel.onClickReplaceAll)
        }
    }

}
package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.stack_loading_item.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.stackloading.StackItemViewModel

class StackLoadingItemView : AndroidView<StackItemViewModel>() {

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
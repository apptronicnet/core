package net.apptronic.test.commons_sample_app.binders

import android.view.View
import kotlinx.android.synthetic.main.throttle_test.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.throttle.ThrottleSampleViewModel

class ThrottleSampleViewBinder : ViewBinder<ThrottleSampleViewModel>() {

    override var layoutResId: Int? = R.layout.throttle_test

    override fun onBindView(view: View, viewModel: ThrottleSampleViewModel) {
        with(view) {
            bindClickListener(btnEmitNextItem, viewModel.onClickEmitNewItem)
            bindText(source, viewModel.currentItem)
            bindText(processing, viewModel.processingItem)
            bindText(timer, viewModel.timer)
            bindText(result, viewModel.resultItem)
        }
    }

}
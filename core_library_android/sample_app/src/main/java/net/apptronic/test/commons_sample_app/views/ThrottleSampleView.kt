package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.throttle_test.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.throttle.ThrottleSampleViewModel

class ThrottleSampleView : AndroidView<ThrottleSampleViewModel>() {

    override var layoutResId: Int? = R.layout.throttle_test

    override fun onBindView(view: View, viewModel: ThrottleSampleViewModel) {
        with(view) {
            +(btnEmitNextItem sendClicksTo viewModel.onClickEmitNewItem)
            +(source setTextFrom viewModel.currentItem)
            +(processing setTextFrom viewModel.processingItem)
            +(timer setTextFrom viewModel.timer)
            +(result setTextFrom viewModel.resultItem)
        }
    }

}
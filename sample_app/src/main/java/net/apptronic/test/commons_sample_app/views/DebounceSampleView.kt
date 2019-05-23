package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.debounce_test.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.debounce.DebounceSampleViewModel

class DebounceSampleView : AndroidView<DebounceSampleViewModel>() {

    init {
        layoutResId = R.layout.debounce_test
    }

    override fun onBindView(view: View, viewModel: DebounceSampleViewModel) {
        with(view) {
            +(btnEmitNextItem sendClicksTo viewModel.onClickEmitNewItem)
            +(source setTextFrom viewModel.currentItem)
            +(processing setTextFrom viewModel.processingItem)
            +(timer setTextFrom viewModel.timer)
            +(result setTextFrom viewModel.resultItem)
        }
    }

}
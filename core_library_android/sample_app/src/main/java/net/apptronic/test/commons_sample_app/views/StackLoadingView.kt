package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.stack_loading.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.navigation.stackNavigatorBinding
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setEnabledDisabledFrom
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.core.android.viewmodel.bindings.setVisibleGoneFrom
import net.apptronic.test.commons_sample_app.AppViewFactory
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.stackloading.StackLoadingViewModel

class StackLoadingView : AndroidView<StackLoadingViewModel>() {

    override var layoutResId: Int? = R.layout.stack_loading

    override fun onBindView(view: View, viewModel: StackLoadingViewModel) {
        with(view) {
            +stackNavigatorBinding(container, viewModel.navigator, AppViewFactory)
            +(txtLoading setTextFrom viewModel.loadingIndicatorText)
            +(txtVisible setTextFrom viewModel.visibleIndicatorText)
            +(txtActual setTextFrom viewModel.actualIndicatorText)
            +(btnBack setEnabledDisabledFrom viewModel.isBackButtonEnabled)
            +(btnBack sendClicksTo viewModel.onClickBack)
            +(progressBar setVisibleGoneFrom viewModel.isInProgress)
        }
    }

}
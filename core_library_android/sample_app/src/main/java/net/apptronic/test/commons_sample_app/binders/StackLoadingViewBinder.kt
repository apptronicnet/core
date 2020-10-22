package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindEnabledDisabled
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.android.viewmodel.bindings.bindVisibleGone
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.StackLoadingBinding
import net.apptronic.test.commons_sample_app.stackloading.StackLoadingViewModel

class StackLoadingViewBinder : ViewBinder<StackLoadingViewModel>() {

    override var layoutResId: Int? = R.layout.stack_loading

    override fun onBindView() = withBinging(StackLoadingBinding::bind) {
        bindNavigator(container, viewModel.navigator)
        bindText(txtLoading, viewModel.loadingIndicatorText)
        bindText(txtVisible, viewModel.visibleIndicatorText)
        bindText(txtActual, viewModel.actualIndicatorText)
        bindEnabledDisabled(btnBack, viewModel.isBackButtonEnabled)
        bindClickListener(btnBack, viewModel.onClickBack)
        bindVisibleGone(progressBar, viewModel.isInProgress)
    }

}
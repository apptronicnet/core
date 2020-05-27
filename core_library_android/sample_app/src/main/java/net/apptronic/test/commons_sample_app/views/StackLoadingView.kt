package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.stack_loading.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.*
import net.apptronic.core.android.viewmodel.bindings.navigation.bindStackNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.stackloading.StackLoadingViewModel

class StackLoadingView : AndroidView<StackLoadingViewModel>() {

    override var layoutResId: Int? = R.layout.stack_loading

    override fun onBindView(view: View, viewModel: StackLoadingViewModel) {
        with(view) {
            bindStackNavigator(container, viewModel.navigator)
            bindText(txtLoading, viewModel.loadingIndicatorText)
            bindText(txtVisible, viewModel.visibleIndicatorText)
            bindText(txtActual, viewModel.actualIndicatorText)
            bindEnabledDisabled(btnBack, viewModel.isBackButtonEnabled)
            bindClickListener(btnBack, viewModel.onClickBack)
            bindVisibleGone(progressBar, viewModel.isInProgress)
        }
    }

}
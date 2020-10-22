package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.navigation.bindStackNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.StackNavigationBinding
import net.apptronic.test.commons_sample_app.staknavigation.GestureNavigationViewModel

class GestureNavigationViewBinder : ViewBinder<GestureNavigationViewModel>() {

    override var layoutResId: Int? = R.layout.stack_navigation

    override fun onBindView() = withBinging(StackNavigationBinding::bind) {
        bindStackNavigator(navigationContainer, viewModel.navigator)
        bindClickListener(btnAdd, viewModel::onClickAdd)
        bindClickListener(btnReplace, viewModel::onClickReplace)
        bindClickListener(btnReplaceAll, viewModel::onClickReplaceAll)
        bindClickListener(btnBack, viewModel::onClickBack)
    }

}
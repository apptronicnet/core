package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.screen_navigation.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.navigation.NavigationScreenViewModel

class NavigationScreenViewBinder : ViewBinder<NavigationScreenViewModel>() {

    override var layoutResId: Int? = R.layout.screen_navigation

    override fun onBindView(view: View, viewModel: NavigationScreenViewModel) {
        with(view) {
            bindClickListener(loginScreenDemo, viewModel::onClickLoginDemo)
            bindClickListener(converterScreenDemo, viewModel::onClickConverterDemo)
            bindClickListener(listScreenDemo, viewModel::onClickListDemo)
            bindClickListener(pagesScreenDemo, viewModel::onClickPagerDemo)
            bindClickListener(throttleScreenDemo, viewModel::onClickThrottleDemo)
            bindClickListener(lazyListDemo, viewModel::onClickLazyListDemo)
            bindClickListener(filterListDemo, viewModel::onClickListFilterDemo)
            bindClickListener(stackLoadingDemo, viewModel::onClickStackLoadingDemo)
            bindClickListener(stackNavigationDemo, viewModel::onClickStackNavigationModel)
            bindClickListener(lazyFilterListDemo, viewModel::onDynamicFilterListDemo)
        }
    }

}
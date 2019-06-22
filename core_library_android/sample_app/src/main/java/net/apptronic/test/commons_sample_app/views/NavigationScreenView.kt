package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.screen_navigation.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.navigation.NavigationScreenViewModel

class NavigationScreenView : AndroidView<NavigationScreenViewModel>() {

    override var layoutResId: Int? = R.layout.screen_navigation

    override fun onBindView(view: View, viewModel: NavigationScreenViewModel) {
        with(view) {
            +(loginScreenDemo sendClicksTo viewModel::onClickLoginDemo)
            +(converterScreenDemo sendClicksTo viewModel::onClickConverterDemo)
            +(listScreenDemo sendClicksTo viewModel::onClickListDemo)
            +(pagesScreenDemo sendClicksTo viewModel::onClickPagerDemo)
            +(debounceScreenDemo sendClicksTo viewModel::onClickDebounceDemo)
            +(lazyListDemo sendClicksTo viewModel::onClickLazyListDemo)
            +(filterListDemo sendClicksTo viewModel::onClickListFilterDemo)
            +(stackLoadingDemo sendClicksTo viewModel::onClickStackLoadingDemo)
            +(lazyFilterListDemo sendClicksTo viewModel::onDynamicFilterListDemo)
        }
    }

}
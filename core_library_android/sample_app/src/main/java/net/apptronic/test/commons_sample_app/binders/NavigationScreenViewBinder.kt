package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.ScreenNavigationBinding
import net.apptronic.test.commons_sample_app.main.MainScreenViewModel

class NavigationScreenViewBinder : ViewBinder<MainScreenViewModel>() {

    override var layoutResId: Int? = R.layout.screen_navigation

    override fun onBindView() = withBinding(ScreenNavigationBinding::bind) {
        bindClickListener(loginScreenDemo, viewModel::onClickLoginDemo)
        bindClickListener(converterScreenDemo, viewModel::onClickConverterDemo)
        bindClickListener(listScreenDemo, viewModel::onClickListDemo)
        bindClickListener(pagesScreenDemo, viewModel::onClickPagerDemo)
        bindClickListener(throttleScreenDemo, viewModel::onClickThrottleDemo)
        bindClickListener(lazyListDemo, viewModel::onClickLazyListDemo)
        bindClickListener(filterListDemo, viewModel::onClickListFilterDemo)
        bindClickListener(stackLoadingDemo, viewModel::onClickStackLoadingDemo)
        bindClickListener(prevNextNavigationDemo, viewModel::onClickPrevNextNavigationDemo)
        bindClickListener(gestureNavigationDemo, viewModel::onClickGestureNavigationDemo)
        bindClickListener(lazyFilterListDemo, viewModel::onDynamicFilterListDemo)
        bindClickListener(bottomSheetDemo, viewModel::onShowBottomSheetDemo)
        bindClickListener(visibilityDemo, viewModel::onShowVisibilityDemo)
        bindClickListener(animationDemo, viewModel::onShowAnimationDemo)
        bindClickListener(viewTransitionDemo, viewModel::onShowViewTransitionDemo)
        bindClickListener(tabsDemo, viewModel::onClickTabsDemo)
    }

}
package net.apptronic.test.commons_sample_app.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.inject
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.OverlayRouter

class NavigationScreenViewModel(parent: Context, router: NavigationRouter) :
    ViewModel(parent, navigationContext(router)) {

    private val router = inject(NavigationRouterDescriptor)
    private val overlayRouter = inject<OverlayRouter>()

    fun onClickLoginDemo() {
        router.openLoginDemo()
    }

    fun onClickConverterDemo() {
        router.openConverterDemo()
    }

    fun onClickListDemo() {
        router.openListDemo()
    }

    fun onClickPagerDemo() {
        router.openPagerDemo()
    }

    fun onClickThrottleDemo() {
        router.openThrottleDemo()
    }

    fun onClickLazyListDemo() {
        router.openLazyListDemo()
    }

    fun onClickListFilterDemo() {
        router.openListFilterDemo()
    }

    fun onClickStackLoadingDemo() {
        router.openStackLoadingDemo()
    }

    fun onClickPrevNextNavigationDemo() {
        router.openPrevNextNavigationDemo()
    }

    fun onClickGestureNavigationDemo() {
        router.openGestureNavigationDemo()
    }

    fun onDynamicFilterListDemo() {
        router.openDynamicFilterListDemo()
    }

    fun onShowBottomSheetDemo() {
        overlayRouter.showBottomSheet()
    }

    fun onShowVisibilityDemo() {
        router.openVisibilityDemo()
    }

    fun onShowAnimationDemo() {
        router.openAnimationDemo()
    }

}
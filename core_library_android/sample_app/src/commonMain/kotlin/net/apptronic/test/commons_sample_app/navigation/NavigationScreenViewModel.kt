package net.apptronic.test.commons_sample_app.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel

class NavigationScreenViewModel(parent: Context, router: NavigationRouter) :
    ViewModel(parent, navigationContext(router)) {

    private val router = provider().inject(NavigationRouterDescriptor)

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

    fun onDynamicFilterListDemo() {
        router.openDynamicFilterListDemo()
    }

    init {
        doOnVisible {
            doOnCreate {

            }
        }
    }

}
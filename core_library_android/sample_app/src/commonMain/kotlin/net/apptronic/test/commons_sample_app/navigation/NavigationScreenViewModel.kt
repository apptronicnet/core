package net.apptronic.test.commons_sample_app.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class NavigationScreenViewModel(context: ViewModelContext) : ViewModel(context) {

    private val router = getProvider().inject(NavigationContext.NavigationRouterDescriptor)

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

    fun onClickDebounceDemo() {
        router.openDebounceDemo()
    }

    init {
        doOnVisible {
            doOnCreate {

            }
        }
    }

}
package net.apptronic.test.commons_sample_app.main

import net.apptronic.core.commons.navigation.injectNavigationRouter
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.*

fun Contextual.mainScreenViewModel() = MainScreenViewModel(viewModelContext())

class MainScreenViewModel internal constructor(context: ViewModelContext) : ViewModel(context) {

    private val router = injectNavigationRouter()

    fun onClickLoginDemo() {
        router.sendCommands(OpenLoginDemo())
    }

    fun onClickConverterDemo() {
        router.sendCommands(OpenConverterDemo())
    }

    fun onClickListDemo() {
        router.sendCommands(OpenListDemo())
    }

    fun onClickPagerDemo() {
        router.sendCommands(OpenPagerDemo())
    }

    fun onClickThrottleDemo() {
        router.sendCommands(OpenThrottleDemo())
    }

    fun onClickLazyListDemo() {
        router.sendCommands(OpenLazyListDemo())
    }

    fun onClickListFilterDemo() {
        router.sendCommands(OpenListFilterDemo())
    }

    fun onClickStackLoadingDemo() {
        router.sendCommands(OpenStackLoadingDemo())
    }

    fun onClickPrevNextNavigationDemo() {
        router.sendCommands(OpenPrevNextNavigationDemo())
    }

    fun onClickGestureNavigationDemo() {
        router.sendCommands(OpenGestureNavigationDemo())
    }

    fun onDynamicFilterListDemo() {
        router.sendCommands(OpenDynamicFilterListDemo())
    }

    fun onShowBottomSheetDemo() {
        router.sendCommands(ShowBottomSheet())
    }

    fun onShowVisibilityDemo() {
        router.sendCommands(OpenVisibilityDemo())
    }

    fun onShowAnimationDemo() {
        router.sendCommands(OpenAnimationDemo())
    }

    fun onShowViewTransitionDemo() {
        router.sendCommands(OpenViewTransitionDemo())
    }

}
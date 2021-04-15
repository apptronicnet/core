package net.apptronic.test.commons_sample_app.main

import net.apptronic.core.commons.routing.injectNavigationRouter
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.*

fun Contextual.mainScreenViewModel() = MainScreenViewModel(childContext())

class MainScreenViewModel internal constructor(context: Context) : ViewModel(context) {

    private val router = injectNavigationRouter()

    fun onClickLoginDemo() {
        router.sendCommandsSync(OpenLoginDemo())
    }

    fun onClickConverterDemo() {
        router.sendCommandsSync(OpenConverterDemo())
    }

    fun onClickListDemo() {
        router.sendCommandsSync(OpenListDemo())
    }

    fun onClickPagerDemo() {
        router.sendCommandsSync(OpenPagerDemo())
    }

    fun onClickThrottleDemo() {
        router.sendCommandsSync(OpenThrottleDemo())
    }

    fun onClickLazyListDemo() {
        router.sendCommandsSync(OpenLazyListDemo())
    }

    fun onClickListFilterDemo() {
        router.sendCommandsSync(OpenListFilterDemo())
    }

    fun onClickStackLoadingDemo() {
        router.sendCommandsSync(OpenStackLoadingDemo())
    }

    fun onClickPrevNextNavigationDemo() {
        router.sendCommandsSync(OpenPrevNextNavigationDemo())
    }

    fun onClickGestureNavigationDemo() {
        router.sendCommandsSync(OpenGestureNavigationDemo())
    }

    fun onDynamicFilterListDemo() {
        router.sendCommandsSync(OpenDynamicFilterListDemo())
    }

    fun onShowBottomSheetDemo() {
        router.sendCommandsSync(ShowBottomSheet())
    }

    fun onShowVisibilityDemo() {
        router.sendCommandsSync(OpenVisibilityDemo())
    }

    fun onShowAnimationDemo() {
        router.sendCommandsSync(OpenAnimationDemo())
    }

    fun onShowViewTransitionDemo() {
        router.sendCommandsSync(OpenViewTransitionDemo())
    }

    fun onClickTabsDemo() {
        router.sendCommandsSync(OpenTabsDemo())
    }

}
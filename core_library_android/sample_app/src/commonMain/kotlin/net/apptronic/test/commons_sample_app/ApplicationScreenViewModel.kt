package net.apptronic.test.commons_sample_app

import net.apptronic.core.commons.navigation.DefaultNavigationHandler
import net.apptronic.core.commons.navigation.navigationRouter
import net.apptronic.core.commons.navigation.registerNavigationHandler
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.dependencyModule
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.defineViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.stackNavigator
import net.apptronic.test.commons_sample_app.animation.animationDemoViewModel
import net.apptronic.test.commons_sample_app.animation.viewTransitionDemoViewModel
import net.apptronic.test.commons_sample_app.bottomsheet.bottomSheetViewModel
import net.apptronic.test.commons_sample_app.convert.convertScreenViewModel
import net.apptronic.test.commons_sample_app.lazylist.lazyListViewModel
import net.apptronic.test.commons_sample_app.lazylistfiltering.lazyListFilterViewModel
import net.apptronic.test.commons_sample_app.list.listScreenViewModel
import net.apptronic.test.commons_sample_app.loadfilterlist.loadFilterListViewModel
import net.apptronic.test.commons_sample_app.login.loginViewModel
import net.apptronic.test.commons_sample_app.main.mainScreenViewModel
import net.apptronic.test.commons_sample_app.pager.pagerViewModel
import net.apptronic.test.commons_sample_app.registration.registrationViewModel
import net.apptronic.test.commons_sample_app.stackloading.stackLoadingViewModel
import net.apptronic.test.commons_sample_app.staknavigation.gestureNavigationViewModel
import net.apptronic.test.commons_sample_app.staknavigation.prevNextNavigationViewModel
import net.apptronic.test.commons_sample_app.tabs.tabsViewModel
import net.apptronic.test.commons_sample_app.throttle.throttleSampleViewModel
import net.apptronic.test.commons_sample_app.transition.AppTransition
import net.apptronic.test.commons_sample_app.visibility.visibilityDemoViewModel

val ApplicationScreenContext = defineViewModelContext {
    dependencyModule {
        navigationRouter()
    }
}

class ApplicationScreenViewModel(parent: Context) : ViewModel(parent, ApplicationScreenContext),
    DefaultNavigationHandler {

    val appNavigator = stackNavigator()

    val overlayNavigator = stackNavigator()

    init {
        registerNavigationHandler(this)
        appNavigator.set {
            mainScreenViewModel()
        }
    }

    fun onOverlayFadePressed() {
        overlayNavigator.removeLast(AppTransition.BottomSheet)
    }

    fun onBackPressed(): Boolean {
        if (overlayNavigator.removeLast(AppTransition.BottomSheet)) {
            return true
        }
        return appNavigator.popBackStack(BasicTransition.Backward)
    }

    override fun onNavigationCommand(command: Any): Boolean {
        return when (command) {
            is OpenLoginDemo -> navigateForward { loginViewModel() }
            is OpenConverterDemo -> navigateForward { convertScreenViewModel() }
            is OpenListDemo -> navigateForward { listScreenViewModel() }
            is OpenPagerDemo -> navigateForward { pagerViewModel() }
            is OpenThrottleDemo -> navigateForward { throttleSampleViewModel() }
            is OpenLazyListDemo -> navigateForward { lazyListViewModel() }
            is OpenListFilterDemo -> navigateForward { loadFilterListViewModel() }
            is OpenStackLoadingDemo -> navigateForward { stackLoadingViewModel() }
            is OpenPrevNextNavigationDemo -> navigateForward { prevNextNavigationViewModel() }
            is OpenGestureNavigationDemo -> navigateForward { gestureNavigationViewModel() }
            is OpenDynamicFilterListDemo -> navigateForward { lazyListFilterViewModel() }
            is OpenVisibilityDemo -> navigateForward { visibilityDemoViewModel() }
            is OpenAnimationDemo -> navigateForward { animationDemoViewModel() }
            is OpenViewTransitionDemo -> navigateForward { viewTransitionDemoViewModel() }
            is OpenRegistrationScreen -> navigateForward { registrationViewModel(command.registrationListener) }
            is OpenTabsDemo -> navigateForward { tabsViewModel() }
            is BackToLogin -> {
                appNavigator.popBackStack(BasicTransition.Backward)
                true
            }
            is ShowBottomSheet -> {
                overlayNavigator.add(
                    overlayNavigator.navigatorContext.bottomSheetViewModel(),
                    AppTransition.BottomSheet
                )
                true
            }
            is ReplaceBottomSheet -> {
                overlayNavigator.replace(
                    overlayNavigator.navigatorContext.bottomSheetViewModel(),
                    AppTransition.BottomSheet
                )
                true
            }
            else -> false
        }
    }

    private fun navigateForward(builder: Contextual.() -> ViewModel): Boolean {
        appNavigator.add(appNavigator.navigatorContext.builder(), BasicTransition.Forward)
        return true
    }

}
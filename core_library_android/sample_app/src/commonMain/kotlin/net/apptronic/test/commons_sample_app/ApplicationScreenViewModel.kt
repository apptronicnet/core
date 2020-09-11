package net.apptronic.test.commons_sample_app

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.stackNavigator
import net.apptronic.test.commons_sample_app.animation.animationDemoViewModel
import net.apptronic.test.commons_sample_app.bottomsheet.bottomSheetViewModel
import net.apptronic.test.commons_sample_app.convert.ConvertScreenViewModel
import net.apptronic.test.commons_sample_app.lazylist.LazyListViewModel
import net.apptronic.test.commons_sample_app.lazylistfiltering.LazyListFilterViewModel
import net.apptronic.test.commons_sample_app.list.ListScreenViewModel
import net.apptronic.test.commons_sample_app.loadfilterlist.LoadFilterListViewModel
import net.apptronic.test.commons_sample_app.login.LoginRouter
import net.apptronic.test.commons_sample_app.login.LoginViewModel
import net.apptronic.test.commons_sample_app.login.RegistrationListener
import net.apptronic.test.commons_sample_app.navigation.NavigationRouter
import net.apptronic.test.commons_sample_app.navigation.NavigationScreenViewModel
import net.apptronic.test.commons_sample_app.pager.PagerViewModel
import net.apptronic.test.commons_sample_app.registration.RegistrationRouter
import net.apptronic.test.commons_sample_app.registration.RegistrationViewModel
import net.apptronic.test.commons_sample_app.stackloading.StackLoadingViewModel
import net.apptronic.test.commons_sample_app.staknavigation.GestureNavigationViewModel
import net.apptronic.test.commons_sample_app.staknavigation.PrevNextNavigationViewModel
import net.apptronic.test.commons_sample_app.throttle.ThrottleSampleViewModel
import net.apptronic.test.commons_sample_app.transition.AppTransition
import net.apptronic.test.commons_sample_app.visibility.visibilityDemoViewModel

class ApplicationScreenViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    init {
        context.dependencyDispatcher.addInstance<OverlayRouter>(OverlayRouterImpl(this))
    }

    val rootPage = stackNavigator()

    val overlayNavigator = stackNavigator()

    init {
        val router = ApplicationScreenNavigationRouterImpl(this)
        rootPage.set(NavigationScreenViewModel(context, router))
    }

    fun onOverlayFadePressed() {
        overlayNavigator.removeLast(AppTransition.BottomSheet)
    }

    fun onBackPressed(): Boolean {
        if (overlayNavigator.removeLast(AppTransition.BottomSheet)) {
            return true
        }
        return rootPage.popBackStack(BasicTransition.Backward)
    }

}

class ApplicationScreenNavigationRouterImpl(
    private val parent: ApplicationScreenViewModel
) : NavigationRouter {

    override fun openLoginDemo() {
        val router = ApplicationScreenLoginRouterImpl(parent)
        parent.rootPage.add(LoginViewModel(parent.context, router), BasicTransition.Forward)
    }

    override fun openConverterDemo() {
        parent.rootPage.add(
            ConvertScreenViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openListDemo() {
        parent.rootPage.add(
            ListScreenViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openPagerDemo() {
        parent.rootPage.add(
            PagerViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openThrottleDemo() {
        parent.rootPage.add(
            ThrottleSampleViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openLazyListDemo() {
        parent.rootPage.add(
            LazyListViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openListFilterDemo() {
        parent.rootPage.add(
            LoadFilterListViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openStackLoadingDemo() {
        parent.rootPage.add(
            StackLoadingViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openPrevNextNavigationDemo() {
        parent.rootPage.add(
            PrevNextNavigationViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openGestureNavigationDemo() {
        parent.rootPage.add(
            GestureNavigationViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openDynamicFilterListDemo() {
        parent.rootPage.add(
            LazyListFilterViewModel(parent.context),
            BasicTransition.Forward
        )
    }

    override fun openVisibilityDemo() {
        parent.rootPage.add(BasicTransition.Forward) { visibilityDemoViewModel() }
    }

    override fun openAnimationDemo() {
        parent.rootPage.add(BasicTransition.Forward) { animationDemoViewModel() }
    }

}

class ApplicationScreenLoginRouterImpl(
    private val parent: ApplicationScreenViewModel
) : LoginRouter {

    override fun openRegistrationScreen(listener: RegistrationListener) {
        val router = ApplicationScreenRegistrationRouterImpl(parent, listener)
        parent.rootPage.add(
            RegistrationViewModel(parent.context, router),
            BasicTransition.Forward
        )
    }

}

class ApplicationScreenRegistrationRouterImpl(
    private val parent: ApplicationScreenViewModel,
    private val listener: RegistrationListener
) : RegistrationRouter {

    override fun backToLogin(preFilledLogin: String) {
        listener.onRegistrationDone(preFilledLogin)
        parent.rootPage.popBackStack(BasicTransition.Backward)
    }

}

class OverlayRouterImpl(private val target: ApplicationScreenViewModel) : OverlayRouter {

    override fun showBottomSheet() {
        target.overlayNavigator.add(target.bottomSheetViewModel(), AppTransition.BottomSheet)
    }

    override fun replaceBottomSheet() {
        target.overlayNavigator.replace(target.bottomSheetViewModel(), AppTransition.BottomSheet)
    }

}
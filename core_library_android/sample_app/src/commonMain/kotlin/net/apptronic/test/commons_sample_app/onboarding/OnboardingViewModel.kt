package net.apptronic.test.commons_sample_app.onboarding

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.inject
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.onboarding.balance.BalanceRouter
import net.apptronic.test.commons_sample_app.onboarding.balance.BalanceViewModel
import net.apptronic.test.commons_sample_app.onboarding.currency.CurrencyRouter
import net.apptronic.test.commons_sample_app.onboarding.currency.CurrencyViewModel

class OnboardingViewModel(parent: Context) : ViewModel(parent, OnboardingContext) {

    val data = inject(OnboardingDataDescriptor)

    val stepScreen = stackNavigator()

    init {
        val currencyRouter = CurrencyRouterImpl(this)
        val currencyViewModel = CurrencyViewModel(context, currencyRouter)
        stepScreen.set(currencyViewModel)
    }

    fun openBalanceScreen() {
        val balanceRouter = BalanceRouterImpl(this)
        val currencyViewModel = BalanceViewModel(context, balanceRouter)
        stepScreen.add(currencyViewModel)
    }

    fun openBudgetTypeScreen() {

    }

}

class CurrencyRouterImpl(val target: OnboardingViewModel) : CurrencyRouter {

    override fun onContinueClick() {
        target.openBalanceScreen()
    }

}

class BalanceRouterImpl(val target: OnboardingViewModel) : BalanceRouter {

    override fun onContinueClick() {
        target.openBudgetTypeScreen()
    }

}
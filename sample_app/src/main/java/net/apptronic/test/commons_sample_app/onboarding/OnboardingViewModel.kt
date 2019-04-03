package net.apptronic.test.commons_sample_app.onboarding

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.onboarding.OnboardingContext.Companion.OnboardingDataDescriptor
import net.apptronic.test.commons_sample_app.onboarding.balance.BalanceRouter
import net.apptronic.test.commons_sample_app.onboarding.balance.BalanceViewModel
import net.apptronic.test.commons_sample_app.onboarding.balance.BalanceViewModelContext
import net.apptronic.test.commons_sample_app.onboarding.currency.CurrencyRouter
import net.apptronic.test.commons_sample_app.onboarding.currency.CurrencyViewModel
import net.apptronic.test.commons_sample_app.onboarding.currency.CurrencyViewModelContext

class OnboardingViewModel(context: OnboardingContext) : ViewModel(context) {

    val data = getProvider().inject(OnboardingDataDescriptor)

    val stepScreen = stackNavigator()

    init {
        val currencyContext = CurrencyViewModelContext(this, CurrencyRouterImpl(this))
        val currencyViewModel = CurrencyViewModel(currencyContext)
        stepScreen.set(currencyViewModel)
    }

    fun openBalanceScreen() {
        val balanceContext = BalanceViewModelContext(this, BalanceRouterImpl(this))
        val currencyViewModel = BalanceViewModel(balanceContext)
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
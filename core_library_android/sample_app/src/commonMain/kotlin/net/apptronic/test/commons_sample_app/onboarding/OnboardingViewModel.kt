package net.apptronic.test.commons_sample_app.onboarding

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.stackNavigator
import net.apptronic.test.commons_sample_app.onboarding.balance.BalanceRouter
import net.apptronic.test.commons_sample_app.onboarding.balance.balanceViewModel
import net.apptronic.test.commons_sample_app.onboarding.currency.CurrencyRouter
import net.apptronic.test.commons_sample_app.onboarding.currency.currencyViewModel

fun Contextual.onboardingViewModel() =
    OnboardingViewModel(
        childContext {
            dependencyModule(OnboardingModule)
        }
    )

class OnboardingViewModel(context: Context) : ViewModel(context) {

    val data = inject(OnboardingDataDescriptor)

    val stepScreen = stackNavigator()

    init {
        val currencyRouter = CurrencyRouterImpl(this)
        stepScreen.set(currencyViewModel(currencyRouter))
    }

    fun openBalanceScreen() {
        val balanceRouter = BalanceRouterImpl(this)
        stepScreen.add(balanceViewModel(balanceRouter))
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
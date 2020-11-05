package net.apptronic.test.commons_sample_app.onboarding.balance

import net.apptronic.core.context.Context
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.setAs
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.defineViewModelContext
import net.apptronic.test.commons_sample_app.app.HttpClientDescriptor
import net.apptronic.test.commons_sample_app.onboarding.OnboardingDataDescriptor

private fun balanceContext(router: BalanceRouter) = defineViewModelContext {
    dependencyDispatcher.addInstance(router)
}

class BalanceViewModel(parent: Context, router: BalanceRouter) :
    ViewModel(parent, balanceContext(router)) {

    private val data = inject(OnboardingDataDescriptor)
    private val router = inject<BalanceRouter>()
    private val httpClient = inject(HttpClientDescriptor)

    val balance = value("")

    val isError = value(false).setAs(
        balance.map {
            try {
                it.toDouble()
                false
            } catch (e: NumberFormatException) {
                true
            }
        }
    )

    private val balanceParsed = value<Double?>().setAs(
        balance.map {
            try {
                it.toDouble()
            } catch (e: NumberFormatException) {
                null
            }
        }
    )

    val isContinueButtonEnabled = value(false).setAs(
        balanceParsed.map {
            it != null
        }
    )

    val isContinueButtonClick = genericEvent()

    init {
        isContinueButtonClick.subscribe {
            data.accountBalance = balanceParsed.get()
            router.onContinueClick()
        }
    }

}
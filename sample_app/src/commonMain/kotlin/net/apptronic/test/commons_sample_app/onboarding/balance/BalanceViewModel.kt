package net.apptronic.test.commons_sample_app.onboarding.balance

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.app.ApplicationContext
import net.apptronic.test.commons_sample_app.onboarding.OnboardingContext

class BalanceViewModel(context: BalanceViewModelContext) : ViewModel(context) {

    private val data = getProvider().inject(OnboardingContext.OnboardingDataDescriptor)
    private val router = getProvider().inject<BalanceRouter>()
    private val httpClient = getProvider().inject(ApplicationContext.HttpClientDescriptor)

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
package net.apptronic.test.commons_sample_app.onboarding.currency

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.onboarding.OnboardingContext

class CurrencyViewModel(context: CurrencyViewModelContext) : ViewModel(context) {

    private val data = getProvider().inject(OnboardingContext.OnboardingDataDescriptor)
    private val router = getProvider().inject<CurrencyRouter>()

    val listOfCurrencies = value<List<Int>>()

    val selectedCurrencyId = value<Int>()

    val isContinueButtonEnabled = value(false)

    val continueButtonClick = genericEvent()

    init {
        selectedCurrencyId.subscribe {
            data.currencyId = it
            isContinueButtonEnabled.set(true)
        }
        continueButtonClick.subscribe {
            router.onContinueClick()
        }
    }

}
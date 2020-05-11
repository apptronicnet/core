package net.apptronic.test.commons_sample_app.onboarding.currency

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EMPTY_VIEW_MODEL_CONTEXT
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.onboarding.OnboardingDataDescriptor

class CurrencyViewModel(parent: Context, private val router: CurrencyRouter) :
    ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT) {

    private val data = inject(OnboardingDataDescriptor)

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
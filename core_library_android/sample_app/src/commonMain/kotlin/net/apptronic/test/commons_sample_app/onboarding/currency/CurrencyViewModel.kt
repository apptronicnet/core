package net.apptronic.test.commons_sample_app.onboarding.currency

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.onboarding.OnboardingDataDescriptor

fun Contextual.currencyViewModel(router: CurrencyRouter) =
    CurrencyViewModel(childContext(), router)

class CurrencyViewModel(context: Context, private val router: CurrencyRouter) :
    ViewModel(context) {

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
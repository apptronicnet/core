package net.apptronic.test.commons_sample_app.onboarding.balance

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.setAs
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.app.HttpClientDescriptor
import net.apptronic.test.commons_sample_app.onboarding.OnboardingDataDescriptor

fun Contextual.balanceViewModel(router: BalanceRouter) =
    BalanceViewModel(
        childContext {
            dependencyDispatcher.addInstance(router)
        }
    )

class BalanceViewModel internal constructor(context: Context) : ViewModel(context) {

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
package net.apptronic.test.commons_sample_app.onboarding.currency

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class CurrencyViewModelContext(
    parent: Context,
    router: CurrencyRouter
) : ViewModelContext(parent) {

    init {
        getProvider().addInstance(router)
    }

}
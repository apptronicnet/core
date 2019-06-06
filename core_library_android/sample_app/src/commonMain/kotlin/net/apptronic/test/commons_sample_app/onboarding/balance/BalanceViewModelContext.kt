package net.apptronic.test.commons_sample_app.onboarding.balance

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class BalanceViewModelContext(
    parent: Context,
    router: BalanceRouter
) : ViewModelContext(parent) {

    init {
        getProvider().addInstance(router)
    }

}
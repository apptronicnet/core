package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.dispatcher.BaseViewModelDispatcher
import net.apptronic.test.commons_sample_app.ApplicationScreenViewModel

class AppUI(context: Context) : BaseViewModelDispatcher<ApplicationScreenViewModel>(context) {

    override fun onCreateViewModelRequested(): ApplicationScreenViewModel {
        return ApplicationScreenViewModel(context)
    }

}
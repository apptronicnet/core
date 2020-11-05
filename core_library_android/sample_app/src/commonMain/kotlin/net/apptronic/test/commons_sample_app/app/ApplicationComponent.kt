package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.viewmodel.dispatcher.ViewModelDispatcher
import net.apptronic.core.viewmodel.dispatcher.viewModelDispatcher
import net.apptronic.test.commons_sample_app.ApplicationScreenViewModel

class ApplicationComponent(context: Context) : Component(context) {

    val appUI: ViewModelDispatcher<ApplicationScreenViewModel> = viewModelDispatcher {
        ApplicationScreenViewModel(it)
    }

}
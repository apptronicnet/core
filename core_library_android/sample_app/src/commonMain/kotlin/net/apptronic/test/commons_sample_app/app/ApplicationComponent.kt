package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.dispatcher.ViewModelDispatcher
import net.apptronic.core.mvvm.viewmodel.dispatcher.viewModelDispatcher
import net.apptronic.test.commons_sample_app.ApplicationScreenViewModel

class ApplicationComponent(context: Context) : Component(context) {

    val appUI: ViewModelDispatcher<ApplicationScreenViewModel> = viewModelDispatcher {
        ApplicationScreenViewModel(it)
    }

}
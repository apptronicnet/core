package net.apptronic.test.commons_sample_app

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.login.LoginViewModel
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext

class ApplicationScreenViewModel(context: ViewModelContext) : ViewModel(context) {

    val mainScreen = subModelContainer()

    init {
        mainScreen.set(LoginViewModel(LoginViewModelContext(this)))
    }

}
package net.apptronic.test.commons_sample_compat_app.fragments.welcome

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.component.property
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_compat_app.Router

fun Contextual.welcomeViewModel() =
    WelcomeViewModel(
        viewModelContext()
    )

class WelcomeViewModel(context: ViewModelContext) : ViewModel(context) {

    val text = property("Welcome from apptronic.net/core compatibility library")

    val onClickNext = genericEvent()

    init {
        doOnBind {
            val router = inject<Router>()
            onClickNext.subscribe {
                router.goToEnterData()
            }
        }
    }

}
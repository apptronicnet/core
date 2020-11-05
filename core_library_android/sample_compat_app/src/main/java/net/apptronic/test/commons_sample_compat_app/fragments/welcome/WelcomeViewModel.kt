package net.apptronic.test.commons_sample_compat_app.fragments.welcome

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.property
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext
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
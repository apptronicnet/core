package net.apptronic.test.commons_sample_compat_app

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.entities.subscribe
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun Contextual.mainViewModel() = MainViewModel(viewModelContext())

class MainViewModel(context: ViewModelContext) : ViewModel(context) {

    val onClickAbout = genericEvent()
    val onClickDialog = genericEvent()

    init {
        doOnBind {
            val router = inject<Router>()
            onClickAbout.subscribe {
                router.openAbout()
            }
            onClickDialog.subscribe {
                router.openDialog()
            }
        }
    }

}
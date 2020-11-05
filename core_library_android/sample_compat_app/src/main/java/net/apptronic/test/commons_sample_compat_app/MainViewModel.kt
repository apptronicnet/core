package net.apptronic.test.commons_sample_compat_app

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext

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
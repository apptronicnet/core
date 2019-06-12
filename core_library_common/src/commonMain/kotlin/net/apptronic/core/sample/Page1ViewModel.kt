package net.apptronic.core.sample

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun createFirstPage(parent: Context): ViewModel {
    val context = ViewModelContext(parent)
    return Page1ViewModel(context)
}

class Page1ViewModel(context: ViewModelContext) : ViewModel(context) {

    private val router = getProvider().inject(RouterDescriptor)

    val title = value("I am first page")

    val onClickGoToSecond = genericEvent {
        router.goToSecondPage()
    }

}
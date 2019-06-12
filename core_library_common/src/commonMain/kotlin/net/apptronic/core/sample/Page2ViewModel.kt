package net.apptronic.core.sample

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun createSecondPage(parent: Context): ViewModel {
    val context = ViewModelContext(parent)
    return Page2ViewModel(context)
}

class Page2ViewModel(context: ViewModelContext) : ViewModel(context) {

    private val router = getProvider().inject(RouterDescriptor)

    val title = value("I am second page")

    val onClickGoBack = genericEvent {
        router.goBack()
    }

}
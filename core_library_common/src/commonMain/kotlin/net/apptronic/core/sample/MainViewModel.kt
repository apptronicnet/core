package net.apptronic.core.sample

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition

class MainViewModel(context: ViewModelContext) : ViewModel(context), Router {

    val pageNavigator = stackNavigator()

    init {
        pageNavigator.set(createFirstPage(this))
    }

    override fun goToSecondPage() {
        pageNavigator.add(createSecondPage(this), BasicTransition.Forward)
    }

    override fun goBack() {
        pageNavigator.popBackStack(BasicTransition.Back)
    }

}
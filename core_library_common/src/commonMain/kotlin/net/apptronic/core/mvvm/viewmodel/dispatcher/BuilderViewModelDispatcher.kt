package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun <T : ViewModel> Component.viewModelDispatcher(builder: (Context) -> T): ViewModelDispatcher<T> {
    return BuilderViewModelDispatcher(context, builder)
}

private class BuilderViewModelDispatcher<T : ViewModel>(
        context: Context,
        private val builder: (Context) -> T
) : BaseViewModelDispatcher<T>(context) {

    override fun onCreateViewModelRequested(): T {
        return builder(context)
    }

}
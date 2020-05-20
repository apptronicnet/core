package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

inline fun <reified T : ViewModel> Component.viewModelDispatcher(noinline builder: (Context) -> T): ViewModelDispatcher<T> {
    return BuilderViewModelDispatcher(context, T::class, builder)
}

class BuilderViewModelDispatcher<T : ViewModel>(
        context: Context,
        type: KClass<T>,
        private val builder: (Context) -> T
) : BaseViewModelDispatcher<T>(context, type) {

    override fun onCreateViewModelRequested(): T {
        return builder(context)
    }

}
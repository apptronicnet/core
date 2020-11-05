package net.apptronic.core.viewmodel.dispatcher

import net.apptronic.core.context.Context
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.viewmodel.IViewModel
import kotlin.reflect.KClass

inline fun <reified T : IViewModel> IComponent.viewModelDispatcher(noinline builder: (Context) -> T): ViewModelDispatcher<T> {
    return BuilderViewModelDispatcher(context, T::class, builder)
}

class BuilderViewModelDispatcher<T : IViewModel>(
        context: Context,
        type: KClass<T>,
        private val builder: (Context) -> T
) : BaseViewModelDispatcher<T>(context, type) {

    override fun onCreateViewModelRequested(): T {
        return builder(context)
    }

}
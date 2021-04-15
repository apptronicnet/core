package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.IViewModel

fun <T : Any, VM : IViewModel> singleItemViewModelBuilder(
    id: T, builder: (Contextual) -> VM
): ViewModelAdapter<T, T, VM> {
    return SingleItemViewModelAdapter(id, builder)
}

private class SingleItemViewModelAdapter<T : Any, VM : IViewModel>(
    val id: T,
    val builder: (Contextual) -> VM
) : ViewModelAdapter<T, T, VM> {

    override fun getItemId(item: T): T {
        return id
    }

    override fun createViewModel(parent: Contextual, item: T): VM {
        return builder(parent)
    }

}
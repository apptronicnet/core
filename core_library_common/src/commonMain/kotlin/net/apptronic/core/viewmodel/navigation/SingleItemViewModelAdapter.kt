package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.IViewModel

fun <T : Any, VM : IViewModel> singleItemViewModelBuilder(
        id: T, builder: (Context) -> VM
): ViewModelAdapter<T, T, VM> {
    return SingleItemViewModelAdapter(id, builder)
}

private class SingleItemViewModelAdapter<T : Any, VM : IViewModel>(
        val id: T,
        val builder: (Context) -> VM
) : ViewModelAdapter<T, T, VM> {

    override fun getItemId(item: T): T {
        return id
    }

    override fun createViewModel(parent: Context, item: T): VM {
        return builder(parent)
    }

}
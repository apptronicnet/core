package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.IViewModel

fun <T : Any, VM : IViewModel> singleItemViewModelBuilder(
        id: T, builder: (Context) -> VM
): ViewModelBuilder<T, T, VM> {
    return SingleItemViewModelBuilder(id, builder)
}

private class SingleItemViewModelBuilder<T : Any, VM : IViewModel>(
        val id: T,
        val builder: (Context) -> VM
) : ViewModelBuilder<T, T, VM> {

    override fun getId(item: T): T {
        return id
    }

    override fun onCreateViewModel(parent: Context, item: T): VM {
        return builder(parent)
    }

}
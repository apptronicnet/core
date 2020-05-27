package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun <T : Any, VM : ViewModel> singleItemViewModelBuilder(
        id: T, builder: (Context) -> VM
): ViewModelBuilder<T, T, VM> {
    return SingleItemViewModelBuilder(id, builder)
}

private class SingleItemViewModelBuilder<T : Any, VM : ViewModel>(
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
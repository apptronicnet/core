package net.apptronic.core.android.compat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.apptronic.core.component.context.Context

class ContextHolderViewModelFactory(
    private val builder: () -> Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CompatContextHolderViewModel(builder()) as T
    }

}
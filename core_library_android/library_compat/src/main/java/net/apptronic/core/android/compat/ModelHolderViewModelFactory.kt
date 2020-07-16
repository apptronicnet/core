package net.apptronic.core.android.compat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ModelHolderViewModelFactory<VM : net.apptronic.core.mvvm.viewmodel.ViewModel>(
    private val builder: () -> VM
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CompatModelHolderViewModel(builder()) as T
    }

}
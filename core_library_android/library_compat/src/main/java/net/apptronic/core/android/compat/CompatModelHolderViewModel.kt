package net.apptronic.core.android.compat

import androidx.lifecycle.ViewModel
import net.apptronic.core.component.terminate

class CompatModelHolderViewModel<T : net.apptronic.core.mvvm.viewmodel.ViewModel>(
    val coreViewModel: T
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        coreViewModel.terminate()
    }

}
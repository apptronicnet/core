package net.apptronic.core.android.compat

import androidx.lifecycle.ViewModel
import net.apptronic.core.context.component.terminate

class CompatModelHolderViewModel<T : net.apptronic.core.viewmodel.IViewModel>(
    val coreViewModel: T
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        coreViewModel.terminate()
    }

}
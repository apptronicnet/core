package net.apptronic.core.android.compat

import androidx.lifecycle.ViewModel
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.terminate

class CoreCompatContextHolderViewModel(
    val context: Context
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        context.terminate()
    }

}
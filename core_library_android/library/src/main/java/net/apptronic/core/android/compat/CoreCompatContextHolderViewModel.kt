package net.apptronic.core.android.compat

import androidx.lifecycle.ViewModel
import net.apptronic.core.context.Context
import net.apptronic.core.context.terminate

class CoreCompatContextHolderViewModel(
    val context: Context
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        context.terminate()
    }

}
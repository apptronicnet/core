package net.apptronic.core.sample

import net.apptronic.core.component.context.CoreContext
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class RootContext : CoreContext() {

    fun getMainViewModel(): MainViewModel {
        val context = ViewModelContext(this)
        return MainViewModel(context)
    }

}
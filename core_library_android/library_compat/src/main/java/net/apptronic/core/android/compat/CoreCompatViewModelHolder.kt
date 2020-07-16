package net.apptronic.core.android.compat

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

interface CoreCompatViewModelHolder<T : ViewModel> : CoreCompatContextHolder {

    override val componentContext: ViewModelContext

    val viewModel: T


}
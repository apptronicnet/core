package net.apptronic.core.android.compat

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

interface CoreCompatViewModelHolder<T : IViewModel> : CoreCompatContextHolder {

    override val componentContext: ViewModelContext

    val viewModel: T

}
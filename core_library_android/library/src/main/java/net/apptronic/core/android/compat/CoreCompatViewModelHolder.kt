package net.apptronic.core.android.compat

import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModelContext

interface CoreCompatViewModelHolder<T : IViewModel> : CoreCompatContextHolder {

    override val componentContext: ViewModelContext

    val viewModel: T

}
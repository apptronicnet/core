package net.apptronic.core.android.compat

import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.IViewModel

interface CoreCompatViewModelHolder<T : IViewModel> : CoreCompatContextHolder {

    override val componentContext: Context

    val viewModel: T

}
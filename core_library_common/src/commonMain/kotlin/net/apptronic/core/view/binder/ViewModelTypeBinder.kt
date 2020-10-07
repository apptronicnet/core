package net.apptronic.core.view.binder

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.view.CoreViewContext

@UnderDevelopment
abstract class ViewModelTypeBinder<T : IViewModel> : ViewModelBinder<T>(), Contextual {

    // TODO recycle when needed
    final override val context: Context = CoreViewContext()

    abstract fun onBind(viewModel: T)

}
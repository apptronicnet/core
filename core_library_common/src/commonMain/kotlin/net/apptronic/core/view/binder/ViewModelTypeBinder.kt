package net.apptronic.core.view.binder

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.di.DependencyProvider
import net.apptronic.core.view.CoreViewContext
import net.apptronic.core.viewmodel.IViewModel

@UnderDevelopment
abstract class ViewModelTypeBinder<T : IViewModel> : ViewModelBinder<T>(), Contextual {

    // TODO recycle when needed
    final override val context: Context = CoreViewContext()

    abstract fun onBind(viewModel: T)

    override val dependencyProvider: DependencyProvider
        get() = context.dependencyProvider
}
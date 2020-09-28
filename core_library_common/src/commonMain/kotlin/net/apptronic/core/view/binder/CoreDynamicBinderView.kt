package net.apptronic.core.view.binder

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.context.CoreViewContext

/**
 * Multiplatform binding container. Allows to build [ICoreView] to be used by platform for creating platform
 * native layout.
 */
abstract class CoreDynamicBinderView<T : ViewModel>(context: CoreViewContext) : CoreBinderView<T>(context) {

    private val viewModel = value<T>()

    fun withViewModel(): Entity<T> {
        return viewModel
    }

    fun <E> E.withViewModel(action: E.(T) -> Unit) {
        viewModel.subscribe {
            action(it)
        }
    }

    fun withViewModel(viewModel: T) {
        this.viewModel.set(viewModel)
    }

    fun nextViewModel(viewModel: T) {
        this.viewModel.set(viewModel)
    }

}
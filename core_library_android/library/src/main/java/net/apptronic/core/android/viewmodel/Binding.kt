package net.apptronic.core.android.viewmodel

import android.view.View
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel

/**
 * Binding is part of [ViewBinder] some pair of [View] part
 * and [ViewModel] property or properties.
 */
abstract class Binding : BindingContainer {

    private var bindings: Bindings? = null

    internal fun bind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        bindings = Bindings(viewModel, viewBinder)
        onBind(viewModel, viewBinder)
    }

    protected abstract fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>)

    internal fun unbind() {
        bindings!!.unbind()
    }

    override fun onUnbind(action: () -> Unit) {
        bindings!!.onUnbind(action)
    }

    override infix fun add(binding: Binding) {
        bindings!!.add(binding)
    }

}
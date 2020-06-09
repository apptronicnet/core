package net.apptronic.core.android.viewmodel

import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Binding is part of [AndroidView] some pair of [View] part
 * and [ViewModel] property or properties.
 */
abstract class Binding : BindingContainer {

    private var bindings: Bindings? = null

    internal fun bind(viewModel: ViewModel, androidView: AndroidView<*>) {
        bindings = Bindings(viewModel, androidView)
        onBind(viewModel, androidView)
    }

    protected abstract fun onBind(viewModel: ViewModel, androidView: AndroidView<*>)

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
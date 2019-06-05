package net.apptronic.core.android.viewmodel

import net.apptronic.core.mvvm.viewmodel.ViewModel

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
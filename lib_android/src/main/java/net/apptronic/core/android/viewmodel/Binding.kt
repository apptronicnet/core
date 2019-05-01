package net.apptronic.core.android.viewmodel

import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class Binding {

    private val onUnbindActions = mutableListOf<() -> Unit>()

    abstract fun onBind(viewModel: ViewModel, androidView: AndroidView<*>)

    protected fun onUnbind(action: () -> Unit) {
        onUnbindActions.add(action)
    }

    internal fun unbind() {
        onUnbindActions.forEach {
            it.invoke()
        }
        onUnbindActions.clear()
    }

}
package net.apptronic.core.android.viewmodel

abstract class Binding {

    private val onUnbindActions = mutableListOf<() -> Unit>()

    abstract fun onBind()

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
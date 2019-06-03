package net.apptronic.core.android.viewmodel

import net.apptronic.core.mvvm.viewmodel.ViewModel

interface BindingContainer {

    fun onUnbind(action: () -> Unit)

    infix fun add(binding: Binding)

    operator fun Binding.unaryPlus()

}

internal class Bindings(
        val viewModel: ViewModel,
        val androidView: AndroidView<*>
) : BindingContainer {

    private val onUnbindActions = mutableListOf<() -> Unit>()
    private val bindingList = mutableListOf<Binding>()

    internal fun unbind() {
        onUnbindActions.forEach {
            it.invoke()
        }
        bindingList.forEach {
            it.unbind()
        }
        onUnbindActions.clear()
        bindingList.clear()
    }

    override fun onUnbind(action: () -> Unit) {
        onUnbindActions.add(action)
    }

    override infix fun add(binding: Binding) {
        bindingList.add(binding)
        binding.bind(viewModel, androidView)
    }

    override operator fun Binding.unaryPlus() {
        this.add(this)
    }

}
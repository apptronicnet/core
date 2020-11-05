package net.apptronic.core.ios.viewmodel

import net.apptronic.core.viewmodel.IViewModel

interface BindingContainer {

    fun onUnbind(action: () -> Unit)

    fun add(binding: Binding)

    operator fun Binding.unaryPlus() {
        this@BindingContainer.add(this)
    }

}

internal class Bindings(
        val viewModel: IViewModel,
        val viewBinder: ViewBinder<*, *>
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

    override fun add(binding: Binding) {
        bindingList.add(binding)
        binding.bind(viewModel, viewBinder)
    }

}
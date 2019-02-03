package net.apptronic.common.android.mvvm.components.view

import android.view.ViewGroup
import net.apptronic.common.core.component.Component
import net.apptronic.common.core.mvvm.viewmodel.adapter.ViewModelAdapter

class SimpleViewModelAdapter(
    private val container: ViewGroup
) : ViewModelAdapter() {

    override fun onInvalidate(oldModel: Component?, newModel: Component?, transitionInfo: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun createBinding(viewModel: Component): ViewModelBinding<*>? {
        return null
    }

}
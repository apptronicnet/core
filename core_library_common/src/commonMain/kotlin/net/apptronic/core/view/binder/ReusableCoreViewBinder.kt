package net.apptronic.core.view.binder

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.view.CoreView

/**
 * Multiplatform binding container. Allows to build [CoreView] to be used by platform for creating platform
 * native layout.
 */
abstract class ReusableCoreViewBinder<T : ViewModel> : BaseCoreViewBinder<T>(), SupportsTargetBridge<T> {

    private val bridges = mutableListOf<TargetBridge<T>>()

    override fun <E : TargetBridge<T>> addBridge(bridge: E): E {
        bridges.add(bridge)
        return bridge
    }

    override fun unbind() {
        super.unbind()
        bridges.forEach {
            it.releaseTarget()
        }
    }

    final override fun setViewModel(viewModel: ViewModel) {
        super.setViewModel(viewModel)
        val vm = this.viewModel!!
        bridges.forEach {
            it.assignTarget(vm)
        }
        onViewModel(vm)
    }

    final override fun performCreateCoreView() {
        onCreateView()
    }

    abstract fun onCreateView()

    open fun onViewModel(viewModel: T) {

    }

}
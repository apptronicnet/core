package net.apptronic.core.view.binder

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.view.CoreView

/**
 * Multiplatform binding container. Allows to build [CoreView] to be used by platform for creating platform
 * native layout.
 */
abstract class CoreViewBinder<T : ViewModel> : BaseCoreViewBinder<T>() {

    final override fun performCreateCoreView() {
        onBind(viewModel!!)
    }

    abstract fun onBind(viewModel: T)

}
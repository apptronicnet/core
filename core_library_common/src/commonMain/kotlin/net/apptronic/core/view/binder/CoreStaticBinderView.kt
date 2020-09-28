package net.apptronic.core.view.binder

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.context.CoreViewContext

/**
 * Multiplatform binding container. Allows to build [ICoreView] to be used by platform for creating platform
 * native layout.
 */
abstract class CoreStaticBinderView<T : IViewModel>(context: CoreViewContext) : CoreBinderView<T>(context) {

}
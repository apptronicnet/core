package net.apptronic.core.view.binder

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext

abstract class CoreBinderView<T : IViewModel>(final override val context: CoreViewContext) : CoreViewBuilder {

    abstract val view: ICoreView

    final override fun <T : ICoreView> nextView(constructor: (CoreViewContext) -> T, builder: T.() -> Unit): T {
        return super.nextView(constructor, builder)
    }

}
package net.apptronic.core.view.binder

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.view.ICoreParentView
import net.apptronic.core.view.ICoreViewBuilder
import net.apptronic.core.view.ICoreViewRepresentable

abstract class ViewModelBinder<T : IViewModel> : ICoreViewRepresentable, ICoreViewBuilder {

    var parent: ICoreParentView? = null

    final override val viewBuilderParent: ICoreParentView?
        get() = parent

}
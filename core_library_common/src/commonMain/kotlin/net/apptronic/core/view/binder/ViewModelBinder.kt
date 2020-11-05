package net.apptronic.core.view.binder

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.view.ICoreParentView
import net.apptronic.core.view.ICoreViewBuilder
import net.apptronic.core.view.ICoreViewRepresentable
import net.apptronic.core.viewmodel.IViewModel

@UnderDevelopment
abstract class ViewModelBinder<T : IViewModel> : ICoreViewRepresentable, ICoreViewBuilder {

    var parent: ICoreParentView? = null

    final override val viewBuilderParent: ICoreParentView?
        get() = parent

}
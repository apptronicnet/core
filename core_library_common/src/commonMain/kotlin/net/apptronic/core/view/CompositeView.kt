package net.apptronic.core.view

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual

@UnderDevelopment
abstract class CompositeView : ICoreViewRepresentable, ICoreViewBuilder, Contextual {

    // TODO recycle when needed
    final override val context: Context = CoreViewContext()

    var parent: ICoreParentView? = null

    final override val viewBuilderParent: ICoreParentView?
        get() = parent

}
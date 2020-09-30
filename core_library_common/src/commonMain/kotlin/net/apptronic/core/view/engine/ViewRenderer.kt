package net.apptronic.core.view.engine

import net.apptronic.core.component.context.Context
import net.apptronic.core.view.CoreViewContext
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewPropertyConsumer

abstract class ViewRenderer<RenderingContext : IRenderingContext<in ViewType>, CoreView : ICoreView, ViewType>(
        val renderingContext: RenderingContext,
        val coreView: CoreView,
        val view: ViewType
) : ViewPropertyConsumer {

    internal var parentReference: ViewRenderer<RenderingContext, *, in ViewType>? = null

    val parent: ViewRenderer<RenderingContext, *, in ViewType>?
        get() = parentReference

    final override val context: Context = CoreViewContext()

}
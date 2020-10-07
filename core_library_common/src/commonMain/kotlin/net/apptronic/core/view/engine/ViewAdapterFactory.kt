package net.apptronic.core.view.engine

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.view.ICoreView

@UnderDevelopment
interface ViewAdapterFactory<RContext : RenderingContext<View>, CoreView : ICoreView, View : Any, ViewType : View> {

    fun createAdapter(renderingContext: RContext, coreView: CoreView): ViewAdapter<RContext, CoreView, View, ViewType>

}
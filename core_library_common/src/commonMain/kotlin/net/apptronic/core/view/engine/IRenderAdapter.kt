package net.apptronic.core.view.engine

import net.apptronic.core.view.ICoreView

interface IRenderAdapter<RenderingContext : IRenderingContext<in View>, CoreView : ICoreView, View> {

    fun renderView(renderingContext: RenderingContext, coreView: CoreView): ViewRenderer<RenderingContext, CoreView, View>

}
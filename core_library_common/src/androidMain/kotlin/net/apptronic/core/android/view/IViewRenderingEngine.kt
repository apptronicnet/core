package net.apptronic.core.android.view

import net.apptronic.core.android.view.platform.DimensionEngine
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewPropertyConsumer

interface IViewRenderingEngine : ViewPropertyConsumer {

    val androidContext: android.content.Context
    val dimensionEngine: DimensionEngine

    fun render(coreView: ICoreView, independentContext: Boolean): ViewHolder

}
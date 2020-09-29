package net.apptronic.core.android.view

import android.content.Context
import android.view.View
import net.apptronic.core.view.ICoreView

interface ViewTypeAdapter<CoreView : ICoreView, ContentView : View> {

    fun createView(context: Context, source: CoreView): ContentView {
        throw UnsupportedOperationException("Cannot create view for abstract ICoreView $source")
    }

    fun applyViewAttributes(engine: IViewRenderingEngine, coreView: CoreView, frame: View, content: ContentView)

}
package net.apptronic.core.view.engine.base

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.engine.RenderingEngine
import net.apptronic.core.view.engine.ViewAdapter
import net.apptronic.core.view.engine.adapters.*

class TestRenderingEngine : RenderingEngine<TestRenderingContext, TestView>() {

    override val renderingContext: TestRenderingContext = TestRenderingContext()

    init {
        registerFactory(TestFrameContainerViewAdapterFactory)
        registerFactory(TestSpacerViewAdapterFactory)
        registerFactory(TestStackContainerViewAdapterFactory)
        registerFactory(TestTextViewAdapterFactory)
    }

    override fun fallbackAdapter(coreView: ICoreView): ViewAdapter<TestRenderingContext, *, TestView, out TestView> {
        return TestFallbackAdapter(coreView)
    }

}
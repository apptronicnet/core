package net.apptronic.core.view.engine.adapters

import net.apptronic.core.view.containers.CoreFrameContainerView
import net.apptronic.core.view.engine.ViewAdapter
import net.apptronic.core.view.engine.base.TestAdapterFactory
import net.apptronic.core.view.engine.base.TestRenderingContext
import net.apptronic.core.view.engine.base.TestView
import net.apptronic.core.view.engine.base.TestViewAdapter
import net.apptronic.core.view.engine.views.TestFrameContainerView

object TestFrameContainerViewAdapterFactory : TestAdapterFactory<CoreFrameContainerView, TestFrameContainerView> {

    override fun createAdapter(renderingContext: TestRenderingContext, coreView: CoreFrameContainerView): ViewAdapter<TestRenderingContext, CoreFrameContainerView, TestView, TestFrameContainerView> {
        return TestFrameContainerViewRenderer(coreView)
    }

}

class TestFrameContainerViewRenderer(
        override val coreView: CoreFrameContainerView
) : TestViewAdapter<CoreFrameContainerView, TestFrameContainerView>() {

    override fun createView(): TestFrameContainerView {
        return TestFrameContainerView()
    }

    private var current: List<ViewAdapter<*, *, TestView, out TestView>> = emptyList()

    override fun renderView(view: TestFrameContainerView) {
        renderUsing(view, BaseTestViewAdapter(coreView))
        coreView.children.subscribe { list ->
            current.forEach {
                it.terminate()
            }
            current = list.map {
                renderChild(it)
            }
            view.children = current.map { it.view }
            requestRefreshLayout()
        }
    }

}
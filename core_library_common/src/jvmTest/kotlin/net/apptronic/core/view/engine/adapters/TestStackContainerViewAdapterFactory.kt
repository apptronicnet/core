package net.apptronic.core.view.engine.adapters

import net.apptronic.core.view.containers.CoreStackContainerView
import net.apptronic.core.view.engine.ViewAdapter
import net.apptronic.core.view.engine.base.TestAdapterFactory
import net.apptronic.core.view.engine.base.TestRenderingContext
import net.apptronic.core.view.engine.base.TestView
import net.apptronic.core.view.engine.base.TestViewAdapter
import net.apptronic.core.view.engine.views.TestStackContainerView

object TestStackContainerViewAdapterFactory : TestAdapterFactory<CoreStackContainerView, TestStackContainerView> {

    override fun createAdapter(renderingContext: TestRenderingContext, coreView: CoreStackContainerView): ViewAdapter<TestRenderingContext, CoreStackContainerView, TestView, TestStackContainerView> {
        return TestStackContainerViewRenderer(coreView)
    }

}

class TestStackContainerViewRenderer(
        override val coreView: CoreStackContainerView
) : TestViewAdapter<CoreStackContainerView, TestStackContainerView>() {

    override fun createView(): TestStackContainerView {
        return TestStackContainerView()
    }

    private var current: List<ViewAdapter<*, *, TestView, out TestView>> = emptyList()

    override fun renderView(view: TestStackContainerView) {
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
package net.apptronic.core.view.engine.adapters

import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.engine.ViewAdapter
import net.apptronic.core.view.engine.base.TestAdapterFactory
import net.apptronic.core.view.engine.base.TestRenderingContext
import net.apptronic.core.view.engine.base.TestView
import net.apptronic.core.view.engine.base.TestViewAdapter
import net.apptronic.core.view.engine.views.TestSpacerView
import net.apptronic.core.view.widgets.CoreSpacerView

object TestSpacerViewAdapterFactory : TestAdapterFactory<CoreSpacerView, TestSpacerView> {

    override fun createAdapter(renderingContext: TestRenderingContext, coreView: CoreSpacerView)
            : ViewAdapter<TestRenderingContext, CoreSpacerView, TestView, TestSpacerView> {
        return TestSpacerAdapter(coreView)
    }

}

class TestSpacerAdapter(override val coreView: CoreSpacerView) : TestViewAdapter<CoreSpacerView, TestSpacerView>() {

    override fun createView(): TestSpacerView {
        return TestSpacerView()
    }

    override fun renderView(view: TestSpacerView) {
        renderUsing(view, BaseTestViewAdapter(coreView))
        coreView.width.subscribe {
            view.targetWidth = (it as? CoreLayoutDimension.SpecificDimension)?.coreDimension?.toInt() ?: 0
            requestRefreshLayout()
        }
        coreView.height.subscribe {
            view.targetHeight = (it as? CoreLayoutDimension.SpecificDimension)?.coreDimension?.toInt() ?: 0
            requestRefreshLayout()
        }
    }

}
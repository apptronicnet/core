package net.apptronic.core.view.engine.adapters

import net.apptronic.core.view.engine.ViewAdapter
import net.apptronic.core.view.engine.base.TestAdapterFactory
import net.apptronic.core.view.engine.base.TestRenderingContext
import net.apptronic.core.view.engine.base.TestView
import net.apptronic.core.view.engine.base.TestViewAdapter
import net.apptronic.core.view.engine.views.TestTextView
import net.apptronic.core.view.widgets.CoreTextView

object TestTextViewAdapterFactory : TestAdapterFactory<CoreTextView, TestTextView> {

    override fun createAdapter(renderingContext: TestRenderingContext, coreView: CoreTextView)
            : ViewAdapter<TestRenderingContext, CoreTextView, TestView, TestTextView> {
        return TestTextAdapter(coreView)
    }

}

class TestTextAdapter(override val coreView: CoreTextView) : TestViewAdapter<CoreTextView, TestTextView>() {

    override fun createView(): TestTextView {
        return TestTextView()
    }

    override fun renderView(view: TestTextView) {
        renderUsing(view, BaseTestViewAdapter(coreView))
        coreView.text.subscribe {
            view.text = it
            requestRefreshLayout()
        }
    }

}
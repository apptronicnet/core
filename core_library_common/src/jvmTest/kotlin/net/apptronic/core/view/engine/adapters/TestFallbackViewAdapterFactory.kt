package net.apptronic.core.view.engine.adapters

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.engine.base.TestViewAdapter
import net.apptronic.core.view.engine.views.TestTextView

class TestFallbackAdapter(override val coreView: ICoreView) : TestViewAdapter<ICoreView, TestTextView>() {

    override fun createView(): TestTextView {
        return TestTextView()
    }

    override fun renderView(view: TestTextView) {
        renderUsing(view, BaseTestViewAdapter(coreView))
        view.text = "[${coreView::class.simpleName}]"
    }

}
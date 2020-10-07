package net.apptronic.core.view.engine.base

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.engine.ViewAdapter

abstract class TestViewAdapter<CoreView : ICoreView, View : TestView> : ViewAdapter<TestRenderingContext, CoreView, TestView, View>() {

    override val parent: TestViewAdapter<*, in View>?
        get() = super.parent as? TestViewAdapter<*, in View>

    fun requestRefreshLayout() {
        parent?.requestRefreshLayout()
        view.refreshLayout()
    }


}
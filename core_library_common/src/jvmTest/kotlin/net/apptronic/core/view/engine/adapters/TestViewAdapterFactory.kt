package net.apptronic.core.view.engine.adapters

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.engine.ViewAdapter
import net.apptronic.core.view.engine.base.TestAdapterFactory
import net.apptronic.core.view.engine.base.TestRenderingContext
import net.apptronic.core.view.engine.base.TestView
import net.apptronic.core.view.engine.base.TestViewAdapter

object TestViewAdapterFactory : TestAdapterFactory<ICoreView, TestView> {

    override fun createAdapter(renderingContext: TestRenderingContext, coreView: ICoreView):
            ViewAdapter<TestRenderingContext, ICoreView, TestView, TestView> {
        return BaseTestViewAdapter(coreView)
    }

}

class BaseTestViewAdapter(override val coreView: ICoreView) : TestViewAdapter<ICoreView, TestView>() {

    override fun renderView(view: TestView) {
        coreView.paddingLeft.subscribe {
            view.paddingLeft = it.toInt()
            requestRefreshLayout()
        }
        coreView.paddingTop.subscribe {
            view.paddingTop = it.toInt()
            requestRefreshLayout()
        }
        coreView.paddingRight.subscribe {
            view.paddingRight = it.toInt()
            requestRefreshLayout()
        }
        coreView.paddingBottom.subscribe {
            view.paddingBottom = it.toInt()
            requestRefreshLayout()
        }
    }

}
package net.apptronic.core.view.engine.views

import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.engine.base.TestView

class TestFrameContainerView : TestView() {

    var children: List<TestView> = emptyList()

    override val width: Int
        get() {
            val maxChild = if (children.isNotEmpty()) children.maxOf { it.width } else 0
            return maxChild + paddingLeft + paddingRight
        }

    override val height: Int
        get() {
            val maxChild = if (children.isNotEmpty()) children.maxOf { it.height } else 0
            return maxChild + paddingTop + paddingBottom
        }

    override fun refreshLayout() {
        children.forEach {
            it.left = left + paddingLeft
            it.top = top + paddingTop
            it.refreshLayout()
        }
    }

    override fun draw(canvas: TestCanvas) {
        children.forEach {
            it.draw(canvas)
        }
    }

}
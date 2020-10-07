package net.apptronic.core.view.engine.views

import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.engine.base.TestView

class TestStackContainerView : TestView() {

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
        var childTop = top + paddingTop
        children.forEach {
            it.left = left + paddingLeft
            it.top = childTop
            it.refreshLayout()
            childTop += it.height
        }
    }

    override fun draw(canvas: TestCanvas) {
        children.forEach {
            it.draw(canvas)
        }
    }

}
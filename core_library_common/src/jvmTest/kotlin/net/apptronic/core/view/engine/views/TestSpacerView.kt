package net.apptronic.core.view.engine.views

import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.engine.base.TestView

class TestSpacerView : TestView() {

    var targetHeight: Int = 0
    var targetWidth: Int = 0

    override val height: Int
        get() {
            return paddingTop + targetHeight + paddingBottom
        }

    override val width: Int
        get() {
            return paddingLeft + targetWidth + paddingRight
        }

    override fun refreshLayout() {
        // ignore
    }

    override fun draw(canvas: TestCanvas) {
    }

}
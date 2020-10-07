package net.apptronic.core.view.engine.views

import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.engine.base.TestView

class TestTextView : TestView() {

    var text: String = ""

    override val height: Int
        get() {
            return paddingTop + 1 + paddingBottom
        }
    override val width: Int
        get() {
            return paddingLeft + text.length + paddingRight
        }

    override fun refreshLayout() {
        // ignore
    }

    override fun draw(canvas: TestCanvas) {
        with(canvas) {
            draw(paddingLeft, paddingTop, text)
        }
    }

}
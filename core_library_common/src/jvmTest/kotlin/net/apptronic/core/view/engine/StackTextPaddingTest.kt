package net.apptronic.core.view.engine

import net.apptronic.core.view.CompositeView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.containers.stackContainer
import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.widgets.textView
import org.junit.Test

class StackTextPaddingTest : BaseEngineTest() {

    override val canvas: TestCanvas = TestCanvas(12, 5)

    class Content : CompositeView() {

        override fun view(): ICoreView =
                stackContainer {
                    textView {
                        text("Hello 1234")
                        paddingTop(1)
                        paddingLeft(3)
                    }
                    textView {
                        text("Some 456")
                        paddingBottom(1)
                    }
                    textView {
                        text("Another 999")
                    }
                }

    }

    override val view = Content()

    @Test
    fun verifyStack() {
        assertCanvas(
                "            ",
                "   Hello 123",
                "Some 456    ",
                "            ",
                "Another 999 ",
        )
    }

}
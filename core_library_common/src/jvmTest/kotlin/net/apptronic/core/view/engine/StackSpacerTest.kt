package net.apptronic.core.view.engine

import net.apptronic.core.entity.value
import net.apptronic.core.view.CompositeView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.containers.stackContainer
import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.widgets.textView
import org.junit.Test

class StackSpacerTest : BaseEngineTest() {

    override val canvas: TestCanvas = TestCanvas(10, 8)

    class Content : CompositeView() {

        val stackPadding = value<Number>()

        override fun view(): ICoreView =
                stackContainer {
                    orientation(Vertical)
                    stackPadding.subscribe {
                        padding(all = it)
                    }
                    textView {
                        text("Hello")
                    }
                    spacerView(1)
                    textView {
                        text("Hello 2")
                        padding(1)
                    }
                    spacerView(2)
                    textView {
                        text("Hello 3")
                    }
                }

    }

    override val view = Content()

    @Test
    fun verifyStack() {
        assertCanvas(
                "Hello     ",
                "          ",
                "          ",
                " Hello 2  ",
                "          ",
                "          ",
                "          ",
                "Hello 3   ",
        )
    }

}
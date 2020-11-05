package net.apptronic.core.view.engine

import net.apptronic.core.entity.value
import net.apptronic.core.view.CompositeView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.containers.stackContainer
import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.widgets.textView
import org.junit.Test

class StackTextTest : BaseEngineTest() {

    override val canvas: TestCanvas = TestCanvas(12, 4)

    class Content : CompositeView() {

        val text1 = value("")
        val text2 = value("")
        val text3 = value("")

        val stackPadding = value<Number>()

        override fun view(): ICoreView =
                stackContainer {
                    stackPadding.subscribe {
                        padding(all = it)
                    }
                    textView {
                        text(text1)
                    }
                    textView {
                        text(text2)
                    }
                    textView {
                        text(text3)
                    }
                }

    }

    override val view = Content()

    @Test
    fun verifyStack() {
        view.text1.set("Text1")
        view.text2.set("Text2")
        view.text3.set("Text3")
        assertCanvas(
                "Text1       ",
                "Text2       ",
                "Text3       ",
                "            ",
        )

        view.stackPadding.set(1)
        assertCanvas(
                "            ",
                " Text1      ",
                " Text2      ",
                " Text3      ",
        )

    }

}
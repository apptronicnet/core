package net.apptronic.core.view.engine

import net.apptronic.core.entity.commons.value
import net.apptronic.core.view.CompositeView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.containers.frameContainer
import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.widgets.textView
import org.junit.Test

class FrameTextTest : BaseEngineTest() {

    override val canvas: TestCanvas = TestCanvas(12, 4)

    class Content : CompositeView() {

        val text1 = value("")
        val text2 = value("")
        val text3 = value("")

        val framePadding = value<Number>()

        override fun view(): ICoreView =
                frameContainer {
                    framePadding.subscribe {
                        padding(all = it)
                    }
                    textView {
                        text(text1)
                        padding(1)
                    }
                    textView {
                        text(text2)
                        padding(1, left = 6)
                    }
                    textView {
                        text(text3)
                        padding(2)
                    }
                }

    }

    override val view = Content()

    @Test
    fun verifyFrame() {
        view.text1.set("Text1")
        view.text2.set("Text2")
        view.text3.set("Text3")
        assertCanvas(
                "            ",
                " Text1Text2 ",
                "  Text3     ",
                "            ",
        )

        view.text1.set("Text1end")
        view.text2.set("Text2end")
        view.text3.set("Text3end")
        assertCanvas(
                "            ",
                " Text1Text2e",
                "  Text3end  ",
                "            ",
        )

        view.framePadding.set(1)
        assertCanvas(
                "            ",
                "            ",
                "  Text1Text2",
                "   Text3end ",
        )
    }

}
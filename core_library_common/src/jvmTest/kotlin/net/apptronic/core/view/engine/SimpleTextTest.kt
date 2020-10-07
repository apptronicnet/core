package net.apptronic.core.view.engine

import net.apptronic.core.view.CompositeView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.widgets.CoreTextView
import net.apptronic.core.view.widgets.textView
import org.junit.Test

class SimpleTextTest : BaseEngineTest() {

    override val canvas: TestCanvas = TestCanvas(10, 1)

    class Content : CompositeView() {

        lateinit var textView: CoreTextView

        override fun view(): ICoreView =
                textView {
                    textView = this
                }
    }

    override val view = Content()

    @Test
    fun simpleRender() {
        view.textView.text("123")
        draw()
        assertCanvas(
                "123       "
        )
    }

    @Test
    fun renderWithPadding() {
        view.textView.text("Sometext")
        view.textView.paddingLeft(5)
        assertCanvas(
                "     Somet"
        )
    }

}
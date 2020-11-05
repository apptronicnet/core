package net.apptronic.core.view.engine

import net.apptronic.core.context.Contextual
import net.apptronic.core.context.terminate
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ICoreViewRepresentable
import net.apptronic.core.view.engine.base.TestCanvas
import net.apptronic.core.view.engine.base.TestRenderingEngine
import net.apptronic.core.view.engine.base.TestView
import org.junit.After
import org.junit.Before
import kotlin.test.fail

abstract class BaseEngineTest {

    val engine = TestRenderingEngine()
    abstract val canvas: TestCanvas

    abstract val view: ICoreViewRepresentable

    lateinit var viewInstance: ICoreView
    lateinit var contextView: ContextView<TestView>

    @Before
    fun before() {
        viewInstance = view.view()
        contextView = engine.render(viewInstance)
    }

    fun draw() {
        canvas.clear()
        contextView.view.draw(canvas)
    }

    @After
    fun after() {
        contextView.terminate()
        viewInstance.context.terminate()
        (view as? Contextual)?.terminate()
    }

    fun assertCanvas(vararg str: String) {
        draw()
        if (str.size != canvas.canvasHeight) {
            throw IllegalArgumentException("Canvas height is ${canvas.canvasHeight} when assert size is ${str.size}")
        }
        var matches = true
        str.forEachIndexed { index, s ->
            if (s.length != canvas.canvasWidth) {
                throw IllegalArgumentException("Canvas width is ${canvas.canvasWidth} when string[$index] size is ${s.length}")
            }
            val canvasLine = canvas.getString(index)
            if (canvasLine != s) {
                matches = false
            }
        }
        if (!matches) {
            val message = StringBuilder()
            message.appendLine("Canvas state is not matches excepted state")
            message.appendLine("--- EXPECTED ---")
            str.forEach {
                message.append("|")
                message.append(it)
                message.appendLine("|")
            }
            message.appendLine("--- ACTUAL ---")
            canvas.getStrings().forEach {
                message.append("|")
                message.append(it)
                message.appendLine("|")
            }
            message.append("--- END ---")
            fail(message.toString())
        }
    }

}
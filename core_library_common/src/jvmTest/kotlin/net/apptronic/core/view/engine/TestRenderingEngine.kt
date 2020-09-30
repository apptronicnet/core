package net.apptronic.core.view.engine

class TestRenderingEngine() : RenderingEngine<TestRenderingContext, TestView>() {

    override val renderingContext: TestRenderingContext = TestRenderingContext()

    override val adapters: List<IRenderAdapter<TestRenderingContext, *, out TestView>> = listOf(
            // TODO
    )

}
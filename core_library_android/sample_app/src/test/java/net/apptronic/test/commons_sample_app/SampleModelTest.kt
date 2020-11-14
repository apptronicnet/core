package net.apptronic.test.commons_sample_app

import net.apptronic.core.threading.Scheduler
import org.junit.Test
import kotlin.test.assertEquals

class SampleModelTest : LifecycleHolder<FragmentLifecycle> {

    private val lifecycle = FragmentLifecycle()

    override fun localLifecycle() = lifecycle

    override fun threadExecutor(): Scheduler = SynchronousExecutor()

    val model = StartScreenModel(this)

    @Test
    fun shouldSetTitleAfterEachClick() {
        lifecycle.createdStage.enter()
        lifecycle.viewCreatedStage.enter()
        model.onClickRefreshTitle.update()
        assertEquals(model.title.get(), "Title changes 1")
        model.onClickRefreshTitle.update()
        assertEquals(model.title.get(), "Title changes 2")
    }


}
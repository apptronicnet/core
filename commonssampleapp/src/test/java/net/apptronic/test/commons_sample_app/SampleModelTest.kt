package net.apptronic.test.commons_sample_app

import net.apptronic.common.android.mvvm.components.fragment.FragmentLifecycle
import net.apptronic.common.core.mvvm.threading.SynchronousExecutor
import net.apptronic.common.core.mvvm.threading.ThreadExecutor
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.test.commons_sample_app.models.StartScreenModel
import org.junit.Test
import kotlin.test.assertEquals

class SampleModelTest : LifecycleHolder<FragmentLifecycle> {

    private val lifecycle = FragmentLifecycle()

    override fun localLifecycle() = lifecycle

    override fun threadExecutor(): ThreadExecutor = SynchronousExecutor()

    val model = StartScreenModel(this)

    @Test
    fun shouldSetTitleAfterEachClick() {
        lifecycle.createdStage.enter()
        lifecycle.viewCreatedStage.enter()
        model.onClickRefreshTitle.sendEvent()
        assertEquals(model.title.get(), "Title changes 1")
        model.onClickRefreshTitle.sendEvent()
        assertEquals(model.title.get(), "Title changes 2")
    }


}
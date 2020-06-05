package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.record
import org.junit.Test

class ViewModelDispatcherBaseText {

    private class TestViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext)

    private val component = viewModelDispathcerComponent {
        TestViewModel(it)
    }

    @Test
    fun shouldCreateModel() {
        val addedEvents = component.onAdded.record()
        component.attachUi()
        addedEvents.assertSize(1)
    }

    @Test
    fun shouldRetainModel() {
        val addedEvents = component.onAdded.record()
        component.attachUi()
        addedEvents.assertSize(1)
        val model = addedEvents[0]
        component.fullFocus()
        component.fullUnbind()
        component.detachUi()
        component.attachUi()
        addedEvents.assertSize(2)
        val nextModel = addedEvents[1]
        assert(model == nextModel)
    }

    @Test
    fun shouldRecreateModel() {
        val addedEvents = component.onAdded.record()
        component.attachUi()
        addedEvents.assertSize(1)
        val model = addedEvents[0]
        model.closeSelf()

        component.attachUi()
        addedEvents.assertSize(2)
        val nextModel = addedEvents[1]
        assert(model != nextModel)
    }

}
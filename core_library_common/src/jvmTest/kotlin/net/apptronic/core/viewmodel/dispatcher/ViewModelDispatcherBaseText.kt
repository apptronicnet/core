package net.apptronic.core.viewmodel.dispatcher

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.record
import net.apptronic.core.viewmodel.ViewModel
import org.junit.Test

class ViewModelDispatcherBaseText {

    private class TestViewModel(context: Context) : ViewModel(context)

    private val component = viewModelDispatcher {
        TestViewModel(it.childContext())
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
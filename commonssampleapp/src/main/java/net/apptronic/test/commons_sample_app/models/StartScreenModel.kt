package net.apptronic.test.commons_sample_app.models

import net.apptronic.common.android.mvvm.components.fragment.FragmentViewModel
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.Lifecycle
import net.apptronic.test.commons_sample_app.ToolbarTitled

/**
 * Represents sample screen properties, state and events
 */
class StartScreenModel(lifecycle: Lifecycle) : FragmentViewModel(lifecycle), ToolbarTitled {

    /**
     * This is text field with some text
     */
    val someText = value<String>("")

    /**
     * This is text field with text which is length of [someText]
     */
    val someTextLength = value<Int>()

    /**
     * This event is called when user clicked button "Request new input"
     */
    val onUserClickButtonRequestNewInput = genericEvent()

    val onUserClickButtonSelector = genericEvent()

    /**
     * This result listener is for receive new value for someText
     */
    val newInputResultListener = resultListener<String> {
        someText.set(it)
    }

    val selectionResultListener = resultListener<String> { result ->
        doOnceResumed("showToast") {
            showToastEvent.sendEvent(result)
        }
    }

    val showToastEvent = typedEvent<String>()

    init {
        someText.subscribe { lastInputValue ->
            someTextLength.set(lastInputValue.length)
        }
    }

    // do not watch on this

    override fun getToolbarTitle(): String {
        return "Main screen"
    }

}
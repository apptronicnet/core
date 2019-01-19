package net.apptronic.test.commons_sample_app.models

import net.apptronic.common.android.ui.components.fragment.FragmentViewModel
import net.apptronic.common.android.ui.components.submodels.TextLabelModel
import net.apptronic.common.android.ui.viewmodel.entity.setup
import net.apptronic.common.android.ui.viewmodel.extensions.asFunctionOf
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.test.commons_sample_app.ToolbarTitled

/**
 * Represents sample screen properties, state and events
 */
class StartScreenModel(lifecycle: Lifecycle) : FragmentViewModel(lifecycle), ToolbarTitled {

    val lastInput = TextLabelModel(this)

    val lastInputLength = TextLabelModel(this).apply {
        text.setup {
            asFunctionOf(lastInput.text) {
                it.length.toString()
            }
        }
    }

    override fun getToolbarTitle(): String {
        return "Main screen"
    }

    val requestNewInputEvent = genericEvent()

    val newInputResultListener = resultListener<String> {
        lastInput.text.set(it)
    }

}
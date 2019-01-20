package net.apptronic.test.commons_sample_app.models

import net.apptronic.common.android.ui.components.fragment.FragmentViewModel
import net.apptronic.common.android.ui.components.submodels.TextInputModel
import net.apptronic.common.android.ui.viewmodel.entity.setup
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.test.commons_sample_app.ToolbarTitled

class NewInputScreenModel(
    lifecycle: Lifecycle,
    private val resultCallback: (String) -> Unit
) : FragmentViewModel(lifecycle), ToolbarTitled {

    val newInput = TextInputModel(this)

    val submitBtnClicked = genericEvent().setup {
        subscribe {
            resultCallback(newInput.getText())
            closeSelf()
        }
    }

    override fun getToolbarTitle(): String {
        return "New input"
    }

}
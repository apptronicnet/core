package net.apptronic.test.commons_sample_app

import net.apptronic.common.android.mvvm.components.activity.ActivityViewModel
import net.apptronic.common.android.mvvm.components.fragment.FragmentLifecycle
import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.test.commons_sample_app.models.NewInputScreenModel
import net.apptronic.test.commons_sample_app.models.StartScreenModel
import net.apptronic.test.commons_sample_app.models.YesNoSelectorViewModel

class MainViewModel(lifecycle: Lifecycle) : ActivityViewModel(lifecycle) {

    val currentRootScreen = modelStack()

    init {
        openStartScreen()
    }

    val toolbarTitle = value<String>().setup {
        asFunctionOf(currentRootScreen) {
            (it as? ToolbarTitled)?.getToolbarTitle() ?: "Sample app"
        }
    }

    private fun openStartScreen() {
        val model = StartScreenModel(FragmentLifecycle(this))
        model.onUserClickButtonRequestNewInput.subscribe {
            openRequestNewInputScreen(model.newInputResultListener)
        }
        model.onUserClickButtonSelector.subscribe {
            openSelectorScreen(model.selectionResultListener)
        }
        currentRootScreen.add(model)
    }

    private fun openRequestNewInputScreen(resultListener: ResultListener<String>) {
        currentRootScreen.add(
            NewInputScreenModel(FragmentLifecycle(this), resultListener)
        )
    }

    private fun openSelectorScreen(resultListener: ResultListener<String>) {
        currentRootScreen.add(
            YesNoSelectorViewModel(FragmentLifecycle(this), resultListener)
        )
    }

    fun onBackPressed(): Boolean {
        return if (currentRootScreen.getSize() > 1) {
            currentRootScreen.popBackStack()
            true
        } else {
            false
        }
    }

}
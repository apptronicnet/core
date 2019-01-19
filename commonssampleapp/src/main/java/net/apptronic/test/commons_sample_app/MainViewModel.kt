package net.apptronic.test.commons_sample_app

import net.apptronic.common.android.ui.components.activity.ActivityViewModel
import net.apptronic.common.android.ui.components.fragment.FragmentLifecycle
import net.apptronic.common.android.ui.viewmodel.entity.ResultListener
import net.apptronic.common.android.ui.viewmodel.entity.setup
import net.apptronic.common.android.ui.viewmodel.extensions.asFunctionOf
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.test.commons_sample_app.models.NewInputScreenModel
import net.apptronic.test.commons_sample_app.models.StartScreenModel

class MainViewModel(lifecycle: Lifecycle) : ActivityViewModel(lifecycle) {

    val rootModel = modelStack()

    val toolbarTitle = value<String>().setup {
        asFunctionOf(rootModel) {
            (it as? ToolbarTitled)?.getToolbarTitle() ?: "Sample app"
        }
    }

    init {
        openMainScreen()
    }

    private fun openMainScreen() {
        val model = StartScreenModel(FragmentLifecycle(this))
        model.requestNewInputEvent.subscribe {
            openRequestNewInputScreen(model.newInputResultListener)
        }
        rootModel.add(model)
    }

    private fun openRequestNewInputScreen(resultListener: ResultListener<String>) {
        rootModel.add(NewInputScreenModel(FragmentLifecycle(this)) {
            resultListener.setResult(it)
            rootModel.popBackStack()
        })
    }

    fun onBackPressed(): Boolean {
        return if (rootModel.getSize() > 1) {
            rootModel.popBackStack()
            true
        } else {
            false
        }
    }


}
package net.apptronic.test.commons_sample_app

import net.apptronic.common.android.ui.components.activity.ActivityViewModel
import net.apptronic.common.android.ui.components.fragment.FragmentLifecycle
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.test.commons_sample_app.models.StartScreenModel

class MainViewModel(lifecycle: Lifecycle) : ActivityViewModel(lifecycle) {

    val rootModel = innerModel()

    init {
        rootModel.add(
            StartScreenModel(
                FragmentLifecycle(
                    this
                )
            )
        )
    }

}
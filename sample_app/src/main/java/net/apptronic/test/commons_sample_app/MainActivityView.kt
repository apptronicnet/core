package net.apptronic.test.commons_sample_app

import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewModelAdapter

class MainActivityView : AndroidView<ApplicationScreenViewModel>() {

    override fun onBindView(view: View, viewModel: ApplicationScreenViewModel) {
        with(view) {
            viewModel.rootPage.setAdapter(AndroidViewModelAdapter(container, MainModelFactory))
        }
    }

}
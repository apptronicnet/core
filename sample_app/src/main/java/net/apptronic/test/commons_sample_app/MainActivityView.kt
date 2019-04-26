package net.apptronic.test.commons_sample_app

import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewModelAdapter
import net.apptronic.core.android.viewmodel.ViewModelBinding

class MainActivityView : AndroidView<ApplicationScreenViewModel>() {

    override fun onCreateBinding(
        view: View,
        viewModel: ApplicationScreenViewModel
    ): ViewModelBinding<ApplicationScreenViewModel> {
        return createBinding(view, viewModel) {
            with(view) {
                viewModel.rootPage.setAdapter(AndroidViewModelAdapter(container, MainModelFactory))
            }
        }
    }

}
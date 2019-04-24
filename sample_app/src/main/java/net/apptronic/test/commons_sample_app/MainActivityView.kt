package net.apptronic.test.commons_sample_app

import kotlinx.android.synthetic.main.activity_main.view.*
import net.apptronic.core.android.viewmodel.AndroidActivityView

class MainActivityView(viewModel: ApplicationScreenViewModel) :
    AndroidActivityView<ApplicationScreenViewModel>(viewModel) {

    override fun onBindView() {
        with(getView()) {
            viewModel.rootPage.setAdapter(MainModelAdapter(container))
        }
    }

}
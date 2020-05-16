package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.navigation.stackNavigatorBinding
import net.apptronic.test.commons_sample_app.AppViewFactory
import net.apptronic.test.commons_sample_app.ApplicationScreenViewModel
import net.apptronic.test.commons_sample_app.R

class MainActivityView : AndroidView<ApplicationScreenViewModel>() {

    override var layoutResId: Int? = R.layout.activity_main

    override fun onBindView(view: View, viewModel: ApplicationScreenViewModel) {
        with(view) {
            stackNavigatorBinding(container, viewModel.rootPage)
        }
    }

}
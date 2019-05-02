package net.apptronic.test.commons_sample_app

import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.navigation.stackNavigatorBinding

class MainActivityView : AndroidView<ApplicationScreenViewModel>() {

    init {
        layoutResId = R.layout.activity_main
    }

    override fun onBindView(view: View, viewModel: ApplicationScreenViewModel) {
        with(view) {
            +stackNavigatorBinding(container, viewModel.rootPage, AppViewFactory)
        }
    }

}
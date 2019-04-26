package net.apptronic.test.commons_sample_app.navigation

import android.view.View
import kotlinx.android.synthetic.main.screen_navigation.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.bindings.ClickActionBinding
import net.apptronic.test.commons_sample_app.R

class NavigationScreenView : AndroidView<NavigationScreenViewModel>() {

    init {
        layoutResId = R.layout.screen_navigation
    }

    override fun onCreateBinding(
        view: View,
        viewModel: NavigationScreenViewModel
    ): ViewModelBinding<NavigationScreenViewModel> {
        return createBinding(view, viewModel) {
            with(view) {
                loginScreenDemo.bindTo(ClickActionBinding(), viewModel::onClickLoginDemo)
                converterScreenDemo.bindTo(ClickActionBinding(), viewModel::onClickConverterDemo)
                listScreenDemo.bindTo(ClickActionBinding(), viewModel::onClickListDemo)
            }
        }
    }

}
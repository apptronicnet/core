package net.apptronic.test.commons_sample_app.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.screen_navigation.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.test.commons_sample_app.R

class NavigationScreenView(viewModel: NavigationScreenViewModel) :
    AndroidView<NavigationScreenViewModel>(viewModel) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_navigation, container, false);
    }

    override fun onBindView() {
        with(getView()) {
            loginScreenDemo.setOnClickListener {
                viewModel.onClickLoginDemo()
            }
            converterScreenDemo.setOnClickListener {
                viewModel.onClickConverterDemo()
            }
        }
    }

}
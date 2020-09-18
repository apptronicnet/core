package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import net.apptronic.core.android.anim.animations.ViewAnimation_Fade
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindVisibleGone
import net.apptronic.core.android.viewmodel.bindings.navigation.bindStackNavigator
import net.apptronic.core.component.entity.functions.map
import net.apptronic.test.commons_sample_app.ApplicationScreenViewModel
import net.apptronic.test.commons_sample_app.R

class MainActivityViewBinder : ViewBinder<ApplicationScreenViewModel>() {

    override var layoutResId: Int? = R.layout.activity_main

    override fun onBindView(view: View, viewModel: ApplicationScreenViewModel) {
        with(view) {
            bindStackNavigator(container, viewModel.appNavigator)
            val isFadeOverlayVisible = viewModel.overlayNavigator.map {
                it.visibleModel != null
            }
            fadeOverlay.setOnClickListener {
                viewModel.onOverlayFadePressed()
            }
            bindVisibleGone(
                fadeOverlay,
                isFadeOverlayVisible
            ) {
                animation = ViewAnimation_Fade
            }
            bindStackNavigator(overlayContainer, viewModel.overlayNavigator)
        }
    }

}
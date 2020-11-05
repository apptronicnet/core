package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.anim.animations.ViewAnimation_Fade
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindVisibleGone
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.core.entity.function.isNotNull
import net.apptronic.core.viewmodel.navigation.models.viewModel
import net.apptronic.test.commons_sample_app.ApplicationScreenViewModel
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.ActivityMainBinding

class MainActivityViewBinder : ViewBinder<ApplicationScreenViewModel>() {

    override var layoutResId: Int? = R.layout.activity_main

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onBindView() {
        with(viewBinding) {
            bindNavigator(container, viewModel.appNavigator)
            val isFadeOverlayVisible = viewModel.overlayNavigator.content.viewModel().isNotNull()
            fadeOverlay.setOnClickListener {
                viewModel.onOverlayFadePressed()
            }
            bindVisibleGone(
                fadeOverlay,
                isFadeOverlayVisible
            ) {
                animation = ViewAnimation_Fade
            }
            bindNavigator(overlayContainer, viewModel.overlayNavigator)
        }
    }

}
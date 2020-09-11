package net.apptronic.test.commons_sample_app.views

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.visibility_demo.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindSwitch
import net.apptronic.core.android.viewmodel.bindings.bindVisibleInvisible
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.android.viewmodel.transitions.transformation.alpha
import net.apptronic.core.android.viewmodel.transitions.transformation.transformationTransition
import net.apptronic.core.android.viewmodel.transitions.transformation.translateXToSelf
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.visibility.VisibilityDemoViewModel

class VisibilityDemoViewBinder : ViewBinder<VisibilityDemoViewModel>() {

    override var layoutResId: Int? = R.layout.visibility_demo

    override fun onBindView(view: View, viewModel: VisibilityDemoViewModel) {
        with(view) {
            bindSwitch(swSimple, viewModel.isSimplyVisible)
            bindVisibleInvisible(contentSimple, viewModel.isSimplyVisible.observeState())

            bindSwitch(swSimpleFadeIn, viewModel.isSimplyFadeIn)
            bindVisibleInvisible(
                contentSimpleFadeIn, viewModel.isSimplyFadeIn.observeState(),
                enter = this@VisibilityDemoViewBinder::fadeIn
            )

            bindSwitch(swSimpleFadeOut, viewModel.isSimplyFadeOut)
            bindVisibleInvisible(
                contentSimpleFadeOut, viewModel.isSimplyFadeOut.observeState(),
                exit = this@VisibilityDemoViewBinder::fadeOut
            )

            bindSwitch(swSimpleFades, viewModel.isSimplyFades)
            bindVisibleInvisible(
                contentSimpleFades, viewModel.isSimplyFades.observeState(),
                enter = this@VisibilityDemoViewBinder::fadeIn,
                exit = this@VisibilityDemoViewBinder::fadeOut
            )

            bindSwitch(isVisibleAnimatedSwitch1, viewModel.isVisibleAnimatedSwitch1)
            bindVisibleInvisible(
                isVisibleAnimatedSwitch1Content,
                viewModel.isVisibleAnimatedSwitch1.observeState(),
                builder = {
                    enter {
                        alpha(0f, 1f)

                    }
                    exit {
                        alpha(1f, 0f)

                    }
                })

            bindSwitch(isVisibleAnimatedSwitch2, viewModel.isVisibleAnimatedSwitch2)
            bindVisibleInvisible(
                isVisibleAnimatedSwitch2Content,
                viewModel.isVisibleAnimatedSwitch2.observeState(),
                builder = {
                    enter {
                        translateXToSelf(-1f, 0f)
                        interpolator = DecelerateInterpolator()
                    }
                    exit {
                        translateXToSelf(0f, 1f)
                        interpolator = AccelerateInterpolator()
                    }
                })

            bindSwitch(isVisibleTransitionBuilder1, viewModel.isVisibleTransitionBuilder1)
            bindVisibleInvisible(
                isVisibleTransitionBuilder1Content,
                viewModel.isVisibleTransitionBuilder1.observeState(),
                BasicTransition.Next, TransitionBuilder()
            )

            bindSwitch(isVisibleTransitionBuilder2, viewModel.isVisibleTransitionBuilder2)
            bindVisibleInvisible(
                isVisibleTransitionBuilder2Content,
                viewModel.isVisibleTransitionBuilder2.observeState(),
                BasicTransition.Fade, TransitionBuilder()
            )

        }
    }

    private fun fadeIn(target: View) = transformationTransition(target) {
        alpha(0f, 1f)
    }.withDuration(500)

    private fun fadeOut(target: View) = transformationTransition(target) {
        alpha(1f, 0f)
    }.withDuration(500)

}
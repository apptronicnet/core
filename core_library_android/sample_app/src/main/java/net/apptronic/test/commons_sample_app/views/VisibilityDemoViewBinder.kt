package net.apptronic.test.commons_sample_app.views

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.visibility_demo.view.*
import net.apptronic.core.android.anim.animations.Animation_Fade
import net.apptronic.core.android.anim.animations.ViewSwitch_Fade
import net.apptronic.core.android.anim.animations.ViewSwitch_Next
import net.apptronic.core.android.anim.transformations.*
import net.apptronic.core.android.anim.viewAnimation
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindSwitch
import net.apptronic.core.android.viewmodel.bindings.bindVisibleInvisible
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.visibility.VisibilityDemoViewModel

class VisibilityDemoViewBinder : ViewBinder<VisibilityDemoViewModel>() {

    override var layoutResId: Int? = R.layout.visibility_demo

    private val duration = 700L

    override fun onBindView(view: View, viewModel: VisibilityDemoViewModel) {
        with(view) {
            bindSwitch(chkInterceptAnimations, viewModel.isInterceptAnimations)

            bindSwitch(swSimple, viewModel.isSimplyVisible)
            bindVisibleInvisible(contentSimple, viewModel.isSimplyVisible.observeState())

            bindSwitch(swSimpleFadeIn, viewModel.isSimplyFadeIn)
            bindVisibleInvisible(contentSimpleFadeIn, viewModel.isSimplyFadeIn.observeState()) {
                enter = Animation_Fade
                duration = this@VisibilityDemoViewBinder.duration
                intercept(viewModel.isInterceptAnimations.observeState())
            }

            bindSwitch(swSimpleFadeOut, viewModel.isSimplyFadeOut)
            bindVisibleInvisible(contentSimpleFadeOut, viewModel.isSimplyFadeOut.observeState()) {
                exit = Animation_Fade.reversed()
                duration = this@VisibilityDemoViewBinder.duration
                intercept(viewModel.isInterceptAnimations.observeState())
            }

            bindSwitch(swSimpleFades, viewModel.isSimplyFades)
            bindVisibleInvisible(contentSimpleFades, viewModel.isSimplyFades.observeState()) {
                animation = Animation_Fade
                duration = this@VisibilityDemoViewBinder.duration
                intercept(viewModel.isInterceptAnimations.observeState())
            }

            bindSwitch(isVisibleAnimatedSwitch1, viewModel.isVisibleAnimatedSwitch1)
            bindVisibleInvisible(
                isVisibleAnimatedSwitch1Content,
                viewModel.isVisibleAnimatedSwitch1.observeState()
            ) {
                enter = viewAnimation(DecelerateInterpolator()) {
                    scaleX(0.5f, 1f)
                    scaleY(0.5f, 1f)
                    alpha(0f, 1f)
                }
                exit = viewAnimation(AccelerateInterpolator()) {
                    alpha(1f, 0.5f)
                    translateXToParent(0f, 1f)
                }
                duration = this@VisibilityDemoViewBinder.duration
                intercept(viewModel.isInterceptAnimations.observeState())
            }

            bindSwitch(isVisibleAnimatedSwitch2, viewModel.isVisibleAnimatedSwitch2)
            bindVisibleInvisible(
                isVisibleAnimatedSwitch2Content,
                viewModel.isVisibleAnimatedSwitch2.observeState()
            ) {
                enter = viewAnimation {
                    rotate(-180f, 0f, DecelerateInterpolator())
                    alpha(0f, 1f)
                }
                exit = viewAnimation {
                    rotate(0f, 180f, AccelerateInterpolator())
                    alpha(1f, 0f)
                }
                duration = this@VisibilityDemoViewBinder.duration
                intercept(viewModel.isInterceptAnimations.observeState())
            }

            bindSwitch(isVisibleTransitionBuilder1, viewModel.isVisibleTransitionBuilder1)
            bindVisibleInvisible(
                isVisibleTransitionBuilder1Content,
                viewModel.isVisibleTransitionBuilder1.observeState()
            ) {
                viewSwitch = ViewSwitch_Fade
                duration = this@VisibilityDemoViewBinder.duration
                intercept(viewModel.isInterceptAnimations.observeState())
            }

            bindSwitch(isVisibleTransitionBuilder2, viewModel.isVisibleTransitionBuilder2)
            bindVisibleInvisible(
                isVisibleTransitionBuilder2Content,
                viewModel.isVisibleTransitionBuilder2.observeState()
            ) {
                viewSwitch = ViewSwitch_Next
                duration = this@VisibilityDemoViewBinder.duration
                intercept(viewModel.isInterceptAnimations.observeState())
            }
        }
    }

}
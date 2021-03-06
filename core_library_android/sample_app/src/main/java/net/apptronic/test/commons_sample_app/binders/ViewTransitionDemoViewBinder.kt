package net.apptronic.test.commons_sample_app.binders

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import net.apptronic.core.android.anim.AnimationPlayer
import net.apptronic.core.android.anim.adapter.*
import net.apptronic.core.android.anim.playback
import net.apptronic.core.android.anim.transformations.*
import net.apptronic.core.android.anim.transition.ViewTransitionDirection
import net.apptronic.core.android.anim.transition.ViewTransitionDirectionSpec
import net.apptronic.core.android.anim.transition.viewTransition
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.viewmodel.navigation.BasicTransition
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.animation.ViewTransitionDemoViewModel
import net.apptronic.test.commons_sample_app.databinding.ViewSwitchDemoBinding

private val ReplaceTransition = Any()

class ViewTransitionDemoViewBinder : ViewBinder<ViewTransitionDemoViewModel>() {

    private val duration = 750L

    override var layoutResId: Int? = R.layout.view_switch_demo

    private var index = 1
    private fun backgroundColor(): Int = when (index % 3) {
        1 -> 0xFFFF8888.toInt()
        2 -> 0xFF88FF88.toInt()
        else -> 0xFF8888FF.toInt()
    }

    private lateinit var currentView: View
    private lateinit var player: AnimationPlayer

    private val viewBinding by viewBinding(ViewSwitchDemoBinding::bind)

    private fun View.addButton(
        name: String,
        transitionInfo: Any
    ) = with(viewBinding) {
        val button = LayoutInflater.from(context).inflate(
            R.layout.view_switch_demo_button, controlGrid, false
        ) as Button
        button.text = name
        controlGrid.addView(button)
        button.setOnClickListener {
            playSwitch(transitionInfo)
        }
    }

    private fun View.playSwitch(transitionInfo: Any) = with(viewBinding) {
        index++
        val enteringView = LayoutInflater.from(context)
            .inflate(R.layout.view_switch_demo_item, switchContainer, false) as TextView
        val exitingView = currentView
        enteringView.text = index.toString()
        enteringView.background = ColorDrawable(backgroundColor())
        val transition = LocalAdapter.buildViewTransitionOrEmpty(
            ViewTransitionSpec(
                enteringView,
                exitingView,
                switchContainer,
                duration,
                transitionInfo
            )
        )
        transition.viewAnimationSet.getAnimation(exitingView)?.doOnComplete {
            switchContainer.removeView(exitingView)
        }
        transition.viewAnimationSet.getAnimation(enteringView)?.doOnStart {
            if (transition.direction == ViewTransitionDirection.ExitingOnFront) {
                switchContainer.addView(enteringView, 0)
            } else {
                switchContainer.addView(enteringView)
            }
        }
        transition.viewAnimationSet.playOn(player, true)
        currentView = enteringView
    }

    override fun onBindView() = with(viewBinding) {
        player = AnimationPlayer(view)
        with(view) {
            currentView = firstItem.root
            currentView.background = ColorDrawable(backgroundColor())
            addButton("Next", BasicTransition.Next)
            addButton("Previous", BasicTransition.Previous)
            addButton("Fade", BasicTransition.Fade)
            addButton("Forward", BasicTransition.Forward)
            addButton("Backward", BasicTransition.Backward)
            addButton("Sheet", ReplaceTransition)
        }
    }

    private val ReplaceSheet = viewTransition {
        enter {
            scaleX(0.5f, 1f, AccelerateDecelerateInterpolator().playback(0.5f, 1f))
            scaleY(0.5f, 1f, AccelerateDecelerateInterpolator().playback(0.5f, 1f))
            elevationDp(24f, 0f, 0f, AccelerateDecelerateInterpolator().playback(0.5f, 1f))
            translationZDp(-24f, 0f, 0f, AccelerateDecelerateInterpolator().playback(0.5f, 1f))
            translateYToParent(1f, 0f, DecelerateInterpolator().playback(0f, 0.65f))
        }
        exit {
            alpha(1f, 0f, AccelerateInterpolator().playback(0.7f, 1f))
            elevationDp(0f, 0f, 0f, AccelerateInterpolator().playback(0f, 0.1f))
            translationZDp(0f, 0f, 0f, AccelerateInterpolator().playback(0f, 0.1f))
        }
        order = ViewTransitionDirectionSpec.EnteringOnFront
    }

    private val CustomAdapter = viewTransitionAdapter {
        bindTransition(ReplaceTransition, ReplaceSheet).durationMultiplier(1.8f)
    }

    private val LocalAdapter =
        compositeViewTransitionAdapter(CustomAdapter, BasicViewTransitionAdapter)

}
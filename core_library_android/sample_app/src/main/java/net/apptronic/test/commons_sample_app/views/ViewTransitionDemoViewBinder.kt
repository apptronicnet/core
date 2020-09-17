package net.apptronic.test.commons_sample_app.views

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.view_switch_demo.view.*
import net.apptronic.core.android.anim.AnimationPlayer
import net.apptronic.core.android.anim.playback
import net.apptronic.core.android.anim.transformations.*
import net.apptronic.core.android.anim.transition.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.animation.ViewTransitionDemoViewModel

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

    private fun View.addButton(
        name: String,
        transitionInfo: Any
    ) {
        val button = LayoutInflater.from(context).inflate(
            R.layout.view_switch_demo_button, controlGrid, false
        ) as Button
        button.text = name
        controlGrid.addView(button)
        button.setOnClickListener {
            playSwitch(transitionInfo)
        }
    }

    private fun View.playSwitch(transitionInfo: Any) {
        index++
        val enteringView = LayoutInflater.from(context)
            .inflate(R.layout.view_switch_demo_item, switchContainer, false) as TextView
        val exitingView = currentView
        enteringView.text = index.toString()
        enteringView.background = ColorDrawable(backgroundColor())
        val animationSet = LocalAdapter.buildViewTransition(
            enteringView,
            exitingView,
            switchContainer,
            duration,
            transitionInfo
        )!!
        animationSet.getAnimation(exitingView)?.doOnComplete {
            switchContainer.removeView(exitingView)
        }
        animationSet.getAnimation(enteringView)?.doOnStart {
            if (LocalAdapter.getOrder(transitionInfo) == ViewTransitionOrder.ExitingOnFront) {
                switchContainer.addView(enteringView, 0)
            } else {
                switchContainer.addView(enteringView)
            }
        }
        player.playAnimationSet(animationSet, true)
        currentView = enteringView
    }

    override fun onBindView(view: View, viewModel: ViewTransitionDemoViewModel) {
        player = AnimationPlayer(view)
        with(view) {
            currentView = firstItem as TextView
            currentView.background = ColorDrawable(backgroundColor())
            addButton("Next", BasicTransition.Next)
            addButton("Previous", BasicTransition.Previous)
            addButton("Forward", BasicTransition.Forward)
            addButton("Backward", BasicTransition.Backward)
            addButton("Fade", BasicTransition.Fade)
            addButton("Sheet", ReplaceTransition)
        }
        onUnbind {
            player.recycle()
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
        order = ViewTransitionOrder.EnteringOnFront
    }

    private val CustomAdapter = viewTransitionAdapter {
        bindTransition(ReplaceTransition, ReplaceSheet).durationMultiplier(1.8f)
    }

    private val LocalAdapter = compositeViewTransitionAdapter(CustomAdapter, BasicTransitionAdapter)

}
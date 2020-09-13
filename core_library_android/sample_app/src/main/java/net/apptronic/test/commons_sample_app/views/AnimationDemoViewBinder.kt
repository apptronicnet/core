package net.apptronic.test.commons_sample_app.views

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.animation_demo.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.anim.AnimationPlayer
import net.apptronic.core.android.viewmodel.anim.ViewAnimationDefinition
import net.apptronic.core.android.viewmodel.anim.transformations.*
import net.apptronic.core.android.viewmodel.anim.viewAnimation
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.animation.AnimationDemoViewModel

private val DURATION = 1200L

class AnimationDemoViewBinder : ViewBinder<AnimationDemoViewModel>() {

    override var layoutResId: Int? = R.layout.animation_demo

    private var index = 1
    private fun backgroundColor(): Int = when (index % 3) {
        1 -> 0xFFFF8888.toInt()
        2 -> 0xFF88FF88.toInt()
        else -> 0xFF8888FF.toInt()
    }

    private lateinit var exitAnimation: ViewAnimationDefinition
    private lateinit var enterAnimation: ViewAnimationDefinition

    private fun View.addButtons(
        enterName: String,
        exitName: String,
        animation: ViewAnimationDefinition
    ) {
        radioEnter.addButton(enterName, animation)
        radioExit.addButton(exitName, animation)
    }

    private fun RadioGroup.addButton(
        name: String,
        animation: ViewAnimationDefinition
    ) {
        val radioButton = LayoutInflater.from(context).inflate(
            R.layout.animation_demo_selector, this, false
        ) as RadioButton
        radioButton.text = name
        radioButton.setOnClickListener {
            for (i in 0 until childCount) {
                val item = getChildAt(i) as RadioButton
                item.isChecked = item == radioButton
            }
            if (this.id == R.id.radioEnter) {
                enterAnimation = animation
            }
            if (this.id == R.id.radioExit) {
                exitAnimation = animation.reversed()
            }
        }
        if (childCount == 0) {
            radioButton.performClick()
        }
        addView(radioButton)
    }

    private lateinit var currentView: TextView

    private fun View.playAnimations(player: AnimationPlayer) {
        index++
        val enteringView = LayoutInflater.from(context)
            .inflate(R.layout.animation_demo_item, viewContainer, false) as TextView
        val exitingView = currentView
        enteringView.text = index.toString()
        enteringView.background = ColorDrawable(backgroundColor())
        exitAnimation.createAnimation(exitingView, viewContainer, DURATION)
            .doOnComplete {
                viewContainer.removeView(exitingView)
            }.playOn(player, swInterceptAnimations.isChecked)
        enterAnimation.createAnimation(enteringView, viewContainer, DURATION)
            .doOnStart {
                viewContainer.addView(enteringView)
            }.playOn(player, swInterceptAnimations.isChecked)
        currentView = enteringView
    }

    override fun onBindView(view: View, viewModel: AnimationDemoViewModel) {
        val player = AnimationPlayer(view)
        with(view) {
            currentView = startItem as TextView
            currentView.background = ColorDrawable(backgroundColor())
            btnPlayAnimations.setOnClickListener {
                playAnimations(player)
            }
            addButtons(
                "Fade in",
                "Fade out",
                FadeAnimation
            )
            addButtons(
                "Enter from left",
                "Exit to left",
                FromLeftAnimation
            )
            addButtons(
                "Enter from right",
                "Exit to right",
                FromRightAnimation
            )
            addButtons(
                "Fade from left",
                "Fade to left",
                FadeLeftAnimation
            )
            addButtons(
                "Fade from right",
                "Fade to right",
                FadeRightAnimation
            )
            addButtons(
                "Enter from top",
                "Exit to top",
                ScaleTopAnimation
            )
            addButtons(
                "Enter from bottom",
                "Exit to bottom",
                ScaleBottomAnimation
            )
            addButtons(
                "Roll in left",
                "Roll out left",
                RollLeft
            )
            addButtons(
                "Roll in right",
                "Roll out right",
                RollRight
            )
        }
        onUnbind {
            player.recycle()
        }
    }

}

val FadeAnimation = viewAnimation {
    alpha(0f, 1f)
}

val FromLeftAnimation =
    viewAnimation(DecelerateInterpolator()) { translateXToParent(-1f, 0f) }

val FromRightAnimation =
    viewAnimation(DecelerateInterpolator()) { translateXToParent(1f, 0f) }

val FadeLeftAnimation = viewAnimation {
    translateXToSelf(-1f, 0f, AccelerateDecelerateInterpolator())
    alpha(0f, 1f)
}

val FadeRightAnimation = viewAnimation {
    translateXToSelf(1f, 0f, AccelerateDecelerateInterpolator())
    alpha(0f, 1f)
}

val ScaleTopAnimation = viewAnimation(DecelerateInterpolator()) {
    translateYToParent(-1f, 0f)
    scaleX(0f, 1f)
    scaleY(0f, 1f)
}

val ScaleBottomAnimation = viewAnimation(DecelerateInterpolator()) {
    translateYToParent(1f, 0f)
    scaleX(0f, 1f)
    scaleY(0f, 1f)
}

val RollLeft = viewAnimation(DecelerateInterpolator()) {
    translateXToSelf(-1f, 0f)
    rotate(-360f, 0f)
    alpha(0f, 1f)
}

val RollRight = viewAnimation(DecelerateInterpolator()) {
    translateXToSelf(1f, 0f)
    rotate(360f, 0f)
    alpha(0f, 1f)
}
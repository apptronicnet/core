package net.apptronic.test.commons_sample_app.views

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.animation.*
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.animation_demo.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.anim.AnimationDefinition
import net.apptronic.core.android.viewmodel.anim.AnimationPlayer
import net.apptronic.core.android.viewmodel.anim.TransformationBuilder
import net.apptronic.core.android.viewmodel.anim.defineAnimation
import net.apptronic.core.android.viewmodel.anim.transformations.*
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

    private lateinit var exitAnimation: AnimationDefinition
    private lateinit var enterAnimation: AnimationDefinition

    private fun RadioGroup.addButton(
        name: String,
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: TransformationBuilder.() -> Unit
    ) {
        val animation = defineAnimation(interpolator, buildFlow)
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
                exitAnimation = animation
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
        exitAnimation.createAnimation(exitingView, viewContainer, DURATION).doOnComplete {
            viewContainer.removeView(exitingView)
        }.playOn(player)
        enterAnimation.createAnimation(enteringView, viewContainer, DURATION).doOnStart {
            viewContainer.addView(enteringView)
        }.playOn(player)
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
            with(radioEnter) {
                addButton("Fade in") {
                    alpha(0f, 1f)
                }
                addButton("Enter from left", DecelerateInterpolator()) {
                    translateXToParent(-1f, 0f)
                }
                addButton("Enter from right", DecelerateInterpolator()) {
                    translateXToParent(1f, 0f)
                }
                addButton("Fade from left") {
                    translateXToSelf(-1f, 0f, AccelerateDecelerateInterpolator())
                    alpha(0f, 1f)
                }
                addButton("Fade from right") {
                    translateXToSelf(1f, 0f, AccelerateDecelerateInterpolator())
                    alpha(0f, 1f)
                }
                addButton("Enter from top", DecelerateInterpolator()) {
                    translateYToParent(-1f, 0f)
                    scaleX(0f, 1f)
                    scaleY(0f, 1f)
                }
                addButton("Enter from bottom", DecelerateInterpolator()) {
                    translateYToParent(1f, 0f)
                    scaleX(0f, 1f)
                    scaleY(0f, 1f)
                }
            }
            with(radioExit) {
                addButton("Fade out") {
                    alpha(1f, 0f)
                }
                addButton("Exit to left", AccelerateInterpolator()) {
                    translateXToParent(0f, -1f)
                }
                addButton("Exit to right", AccelerateInterpolator()) {
                    translateXToParent(0f, 1f)
                }
                addButton("Fade to left") {
                    translateXToSelf(0f, -1f, AccelerateDecelerateInterpolator())
                    alpha(1f, 0f)
                }
                addButton("Fade to right") {
                    translateXToSelf(0f, 1f, AccelerateDecelerateInterpolator())
                    alpha(1f, 0f)
                }
                addButton("Exit to top", AccelerateInterpolator()) {
                    translateYToParent(0f, -1f)
                    scaleX(1f, 0f)
                    scaleY(1f, 0f)
                }
                addButton("Exit to bottom", AccelerateInterpolator()) {
                    translateYToParent(0f, 1f)
                    scaleX(1f, 0f)
                    scaleY(1f, 0f)
                }
            }
        }
        onUnbind {
            player.recycle()
        }
    }

}
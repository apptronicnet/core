package net.apptronic.test.commons_sample_app.views

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.view_switch_demo.view.*
import net.apptronic.core.android.anim.AnimationPlayer
import net.apptronic.core.android.anim.ViewSwitchDefinition
import net.apptronic.core.android.anim.animations.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.animation.ViewSwitchDemoViewModel

class ViewSwitchDemoViewBinder : ViewBinder<ViewSwitchDemoViewModel>() {

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
        viewSwitch: ViewSwitchDefinition,
        forward: Boolean
    ) {
        val button = LayoutInflater.from(context).inflate(
            R.layout.view_switch_demo_button, controlGrid, false
        ) as Button
        button.text = name
        controlGrid.addView(button)
        button.setOnClickListener {
            playSwitch(viewSwitch, forward)
        }
    }

    private fun View.playSwitch(viewSwitch: ViewSwitchDefinition, forward: Boolean) {
        index++
        val enteringView = LayoutInflater.from(context)
            .inflate(R.layout.view_switch_demo_item, switchContainer, false) as TextView
        val exitingView = currentView
        enteringView.text = index.toString()
        enteringView.background = ColorDrawable(backgroundColor())
        viewSwitch.exit.createAnimation(exitingView, switchContainer, duration)
            .doOnComplete {
                switchContainer.removeView(exitingView)
            }.playOn(player)
        viewSwitch.enter.createAnimation(enteringView, switchContainer, duration)
            .doOnStart {
                if (forward) {
                    switchContainer.addView(enteringView)
                } else {
                    switchContainer.addView(enteringView, 0)
                }
            }.playOn(player)
        currentView = enteringView
    }

    override fun onBindView(view: View, viewModel: ViewSwitchDemoViewModel) {
        player = AnimationPlayer(view)
        with(view) {
            currentView = firstItem as TextView
            currentView.background = ColorDrawable(backgroundColor())
            addButton("Next", ViewSwitch_Next, true)
            addButton("Previous", ViewSwitch_Previous, false)
            addButton("Forward", ViewSwitch_Forward, true)
            addButton("Backward", ViewSwitch_Backward, false)
            addButton("Fade", ViewSwitch_Fade, true)
        }
        onUnbind {
            player.recycle()
        }
    }

}
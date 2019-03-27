package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition

fun applyEnterTransition(container: ViewGroup, view: View, transition: BasicTransition, time: Int) {
    val animation = when (transition) {
        BasicTransition.Fade -> {
            AlphaAnimation(0f, 1f)
        }
        BasicTransition.Forward -> {
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                1f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f
            ).apply {
                interpolator = AccelerateDecelerateInterpolator()
            }
        }
        BasicTransition.Back -> {
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                -1f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f
            ).apply {
                interpolator = AccelerateDecelerateInterpolator()
            }
        }
    }
    animation.duration = time.toLong()
    container.addView(view)
    view.startAnimation(animation)
}

fun applyExitTransition(container: ViewGroup, view: View, transition: BasicTransition, time: Int) {
    val animation = when (transition) {
        BasicTransition.Fade -> {
            AlphaAnimation(1f, 0f)
        }
        BasicTransition.Forward -> {
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                -1f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f
            ).apply {
                interpolator = AccelerateDecelerateInterpolator()
            }
        }
        BasicTransition.Back -> {
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                1f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f
            ).apply {
                interpolator = AccelerateDecelerateInterpolator()
            }
        }
    }
    animation.duration = time.toLong()
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) {
            container.post {
                container.removeView(view)
            }
        }

        override fun onAnimationRepeat(animation: Animation?) {
            // unused
        }

        override fun onAnimationStart(animation: Animation?) {
            // unused
        }
    })
    view.startAnimation(animation)
}
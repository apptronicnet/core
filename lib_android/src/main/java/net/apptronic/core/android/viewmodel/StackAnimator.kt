package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition

open class StackAnimator {

    open fun applyEnterTransition(container: ViewGroup, view: View, transition: Any, time: Long) {
        val animation = createEnterAnimation(container, view, transition, time)
        container.addView(view)
        if (animation != null) {
            view.startAnimation(animation)
        }
    }

    open fun createEnterAnimation(container: ViewGroup, view: View, transition: Any, time: Long): Animation? {
        return when (transition) {
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
            else -> null
        }.also {
            it?.duration = time
        }
    }

    open fun applyExitTransition(container: ViewGroup, view: View, transition: Any, time: Long) {
        val animation = createExitTransition(container, view, transition, time)
        if (animation != null) {
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
        } else {
            container.removeView(view)
        }
    }

    open fun createExitTransition(container: ViewGroup, view: View, transition: Any, time: Long): Animation? {
        return when (transition) {
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
            else -> null
        }.also {
            it?.duration = time
        }
    }
}
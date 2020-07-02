package net.apptronic.core.android.utils

import android.view.View
import android.view.animation.Animation

fun Animation.onAnimationEnd(view: View, action: (View) -> Unit) {
    setAnimationListener(
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // ignore
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // ignore
            }

            override fun onAnimationEnd(animation: Animation?) {
                view.post {
                    action.invoke(view)
                }
            }
        }
    )
}
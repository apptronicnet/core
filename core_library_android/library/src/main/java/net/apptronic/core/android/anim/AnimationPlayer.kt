package net.apptronic.core.android.anim

import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import net.apptronic.core.android.anim.animations.ViewAnimation_Empty

class AnimationPlayer(private val rootView: View) : ViewTreeObserver.OnPreDrawListener {

    private val animations = mutableListOf<ViewAnimation>()

    private var viewTreeObserver: ViewTreeObserver? = null

    private fun refreshAttachState() {
        if (viewTreeObserver?.isAlive == false) {
            viewTreeObserver = null
        }
        if (animations.isEmpty() != (viewTreeObserver == null)) {
            if (viewTreeObserver == null) {
                viewTreeObserver = rootView.viewTreeObserver
                viewTreeObserver?.addOnPreDrawListener(this)
            } else {
                viewTreeObserver?.removeOnPreDrawListener(this)
                viewTreeObserver = null
            }
        }
    }

    fun playAnimation(animation: ViewAnimation, intercept: Boolean) {
        Log.d(
            "AnimationPlayer",
            "playAnimation(animation = $animation, intercept = $intercept)"
        )
        animation.start(this, intercept)
    }

    fun cancelAnimations(target: View) {
        val animation = ViewAnimation_Empty.createAnimation(target, rootView, 0)
        playAnimation(animation, true)
    }

    internal fun onAnimationStarted(animation: ViewAnimation) {
        Log.d("AnimationPlayer", "onAnimationStarted(animation = $animation)")
        animations.add(animation)
        rootView.invalidate()
        refreshAttachState()
    }

    fun playAnimation(intercept: Boolean, builder: () -> ViewAnimation) {
        playAnimation(builder(), intercept)
    }

    fun playAnimationSet(set: ViewAnimationSet, intercept: Boolean) {
        set.animations.forEach {
            it.playOn(this, intercept)
        }
    }

    override fun onPreDraw(): Boolean {
        return if (animations.isNotEmpty()) {
            val removed = animations.removeAll {
                val completed = it.playFrame(SystemClock.elapsedRealtime())
                if (completed) {
                    Log.d("AnimationPlayer", "onPreDraw completed $it")
                }
                completed
            }
            if (removed) {
                refreshAttachState()
            }
            !removed
        } else true
    }

}
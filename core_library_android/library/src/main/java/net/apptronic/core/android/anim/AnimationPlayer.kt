package net.apptronic.core.android.anim

import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import net.apptronic.core.android.anim.animations.Animation_Empty

class AnimationPlayer(private val rootView: View) : ViewTreeObserver.OnPreDrawListener {

    private var isAlive = true

    init {
        Log.d("AnimationPlayer", "Initialized on $rootView")
        rootView.viewTreeObserver.addOnPreDrawListener(this)
    }

    private val animations = mutableListOf<ViewAnimation>()

    fun playAnimation(animation: ViewAnimation, intercept: Boolean = true) {
        if (isAlive) {
            Log.d(
                "AnimationPlayer",
                "playAnimation(animation = $animation, intercept = $intercept)"
            )
            animation.start(this, intercept)
        }
    }

    fun cancelAnimations(target: View) {
        val animation = Animation_Empty.createAnimation(target, rootView, 0)
        playAnimation(animation)
    }

    internal fun onAnimationStarted(animation: ViewAnimation) {
        if (isAlive) {
            Log.d("AnimationPlayer", "onAnimationStarted(animation = $animation)")
            animations.add(animation)
            rootView.invalidate()
        }
    }

    fun playAnimation(intercept: Boolean = true, builder: () -> ViewAnimation) {
        playAnimation(builder(), intercept)
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
            !removed
        } else true
    }

    fun recycle() {
        Log.d("AnimationPlayer", "Recycled on $rootView")
        isAlive = false
        rootView.viewTreeObserver.removeOnPreDrawListener(this)
    }

}
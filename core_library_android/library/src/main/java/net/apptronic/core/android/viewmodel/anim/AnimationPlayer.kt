package net.apptronic.core.android.viewmodel.anim

import android.os.SystemClock
import android.view.View
import android.view.ViewTreeObserver

class AnimationPlayer(private val rootView: View) : ViewTreeObserver.OnPreDrawListener {

    init {
        rootView.viewTreeObserver.addOnPreDrawListener(this)
    }

    private val animations = mutableListOf<ViewAnimation>()

    fun playAnimation(animation: ViewAnimation) {
        animation.start()
        animations.add(animation)
    }

    fun playAnimation(builder: () -> ViewAnimation) {
        playAnimation(builder())
    }

    override fun onPreDraw(): Boolean {
        return if (animations.isNotEmpty()) {
            val removed = animations.removeAll {
                it.playFrame(SystemClock.elapsedRealtime())
            }
            !removed
        } else true
    }

    fun recycle() {
        rootView.viewTreeObserver.removeOnPreDrawListener(this)
    }

}
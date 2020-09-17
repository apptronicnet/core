package net.apptronic.core.android.anim

import android.view.animation.Interpolator

class ReverseInterpolator(private val target: Interpolator) : Interpolator {

    override fun getInterpolation(input: Float): Float {
        val reverse = input.reverse()
        val reversedInterpolation = target.getInterpolation(reverse)
        return reversedInterpolation.reverse()
    }

}
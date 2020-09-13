package net.apptronic.core.android.anim

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

fun Interpolator.playback(playbackStart: Float, playbackEnd: Float): Interpolator {
    return PlaybackInterpolator(playbackStart, playbackEnd, this)
}

class PlaybackInterpolator(
    private val playbackStart: Float,
    private val playbackEnd: Float,
    private val target: Interpolator = LinearInterpolator()
) : Interpolator {

    private val playbackWidth = playbackEnd - playbackStart

    override fun getInterpolation(input: Float): Float {
        val playback = when {
            input < playbackStart -> 0f
            input < playbackEnd -> {
                if (playbackWidth > 0f) {
                    (input - playbackStart) / playbackWidth
                } else 1f
            }
            else -> 1f
        }
        return target.getInterpolation(playback)
    }

}
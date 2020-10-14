package net.apptronic.core.android.anim

import android.view.View

class ViewAnimationSet(
    val duration: Long
) {

    internal val animations = mutableListOf<ViewAnimation>()

    fun addAnimation(
        definition: ViewAnimationDefinition, target: View, container: View
    ): ViewAnimation {
        val animation = definition.createAnimation(target, container, duration)
        animations.add(animation)
        return animation
    }

    fun getAnimation(target: View): ViewAnimation? {
        return animations.firstOrNull { it.target == target }
    }

    fun playOn(player: AnimationPlayer, intercept: Boolean) {
        player.playAnimationSet(this, intercept)
    }

}
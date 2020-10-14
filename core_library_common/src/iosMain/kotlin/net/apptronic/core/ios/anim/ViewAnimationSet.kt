package net.apptronic.core.ios.anim

import platform.UIKit.UIView

class ViewAnimationSet(
        val duration: Long
) {

    internal val animations = mutableListOf<ViewAnimation>()

    fun addAnimation(
            definition: ViewAnimationDefinition, target: UIView, container: UIView
    ): ViewAnimation {
        val animation = definition.createAnimation(target, container, duration)
        animations.add(animation)
        return animation
    }

    fun getAnimation(target: UIView): ViewAnimation? {
        return animations.firstOrNull { it.target == target }
    }

    fun playOn(player: AnimationPlayer, intercept: Boolean) {
        player.playAnimationSet(this, intercept)
    }


}
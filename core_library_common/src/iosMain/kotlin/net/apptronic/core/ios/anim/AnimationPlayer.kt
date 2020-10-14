package net.apptronic.core.ios.anim

class AnimationPlayer {

    private val animations = mutableListOf<ViewAnimation>()

    fun playAnimation(animation: ViewAnimation, intercept: Boolean) {
        animation.doOnStart {
            animations.add(animation)
        }
        animation.doOnCompleteOrCancel {
            animations.remove(animation)
        }
        val current = animations.firstOrNull {
            it.target == animation.target
        }
        animation.start(this, intercept, current)
    }

    fun playAnimationSet(set: ViewAnimationSet, intercept: Boolean) {
        set.animations.forEach {
            it.playOn(this, intercept)
        }
    }

}
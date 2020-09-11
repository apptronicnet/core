package net.apptronic.core.android.viewmodel.anim

import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

fun defineAnimation(
    interpolator: Interpolator = LinearInterpolator(),
    buildFlow: TransformationBuilder.() -> Unit
): AnimationDefinition {
    return AnimationDefinition(interpolator, buildFlow)
}

class AnimationDefinition internal constructor(
    private val interpolator: Interpolator,
    private val buildFlow: TransformationBuilder.() -> Unit
) {


    fun createAnimation(target: View, container: View, duration: Long): ViewAnimation {
        val builder = TransformationBuilder()
        builder.buildFlow()
        return ViewAnimation(target, container, builder.set(), duration, interpolator)
    }

    fun withInterpolator(interpolator: Interpolator): AnimationDefinition {
        return AnimationDefinition(interpolator, buildFlow)
    }

}
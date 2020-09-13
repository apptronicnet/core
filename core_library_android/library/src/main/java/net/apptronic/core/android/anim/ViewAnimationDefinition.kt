package net.apptronic.core.android.anim

import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

fun viewAnimation(
    interpolator: Interpolator = LinearInterpolator(),
    buildFlow: TransformationBuilder.() -> Unit
): ViewAnimationDefinition {
    return ViewAnimationDefinition(interpolator, buildFlow)
}

class ViewAnimationDefinition internal constructor(
    private val interpolator: Interpolator,
    private val buildFlow: TransformationBuilder.() -> Unit,
    private val reversed: Boolean = false
) {

    fun createAnimation(
        target: View, container: View, duration: Long
    ): ViewAnimation {
        val builder = TransformationBuilder()
        builder.buildFlow()
        val targetInterpolator = if (!reversed) {
            interpolator
        } else {
            ReverseInterpolator(interpolator)
        }
        val transformationSet = if (!reversed) {
            builder.set()
        } else {
            builder.set().reversed()
        }
        return ViewAnimation(target, container, transformationSet, duration, targetInterpolator)
    }

    fun reversed(): ViewAnimationDefinition {
        return ViewAnimationDefinition(interpolator, buildFlow, reversed.not())
    }

}
package net.apptronic.core.android.anim

import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

fun viewAnimation(
    interpolator: Interpolator = LinearInterpolator(),
    buildFlow: ViewTransformationBuilder.() -> Unit
): ViewAnimationDefinition {
    return ViewAnimationDefinition(interpolator, buildFlow)
}

class ViewAnimationDefinition internal constructor(
    private val interpolator: Interpolator,
    private val buildFlow: ViewTransformationBuilder.() -> Unit,
    private val reversed: Boolean = false
) {

    fun createAnimation(
        target: View, container: View, duration: Long
    ): ViewAnimation {
        val targetInterpolator = if (!reversed) {
            interpolator
        } else {
            ReverseInterpolator(interpolator)
        }
        return ViewAnimation(target, container, this, duration, targetInterpolator)
    }

    fun reversed(): ViewAnimationDefinition {
        return ViewAnimationDefinition(interpolator, buildFlow, reversed.not())
    }

    internal fun createTransformationSet(
        target: View,
        container: View,
        intercepted: Set<ViewTransformationDescriptor>
    ): ViewTransformationSet {
        val builder = ViewTransformationBuilder(target, container, intercepted)
        builder.buildFlow()
        return if (reversed) {
            builder.set().reversed()
        } else {
            builder.set()
        }
    }

}
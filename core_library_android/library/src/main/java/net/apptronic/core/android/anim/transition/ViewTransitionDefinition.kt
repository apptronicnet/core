package net.apptronic.core.android.anim.transition

import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import net.apptronic.core.android.anim.ViewAnimationDefinition
import net.apptronic.core.android.anim.ViewAnimationSet
import net.apptronic.core.android.anim.ViewTransformationBuilder
import net.apptronic.core.android.anim.animations.Animation_Empty
import net.apptronic.core.android.anim.viewAnimation

fun viewTransition(
    enterDefinition: ViewAnimationDefinition,
    exitDefinition: ViewAnimationDefinition,
    order: ViewTransitionOrder = ViewTransitionOrder.Unspecified
): ViewTransitionDefinition {
    return ViewTransitionDefinition(enterDefinition, exitDefinition, order)
}

fun viewTransition(
    transitionBuildFlow: TransitionDefinitionBuilder.() -> Unit
): ViewTransitionDefinition {
    val builder = TransitionDefinitionBuilder()
    builder.transitionBuildFlow()
    return builder.build()
}

class ViewTransitionDefinition internal constructor(
    val enter: ViewAnimationDefinition,
    val exit: ViewAnimationDefinition,
    val order: ViewTransitionOrder
) {

    fun buildAnimationSet(
        enter: View?,
        exit: View?,
        container: View,
        duration: Long
    ): ViewAnimationSet {
        val animationSet = ViewAnimationSet(duration)
        enter?.let {
            animationSet.addAnimation(this.enter, it, container)
        }
        exit?.let {
            animationSet.addAnimation(this.exit, it, container)
        }
        return animationSet
    }

}

class TransitionDefinitionBuilder internal constructor() {

    private var enterDefinition: ViewAnimationDefinition = Animation_Empty
    private var exitDefinition: ViewAnimationDefinition = Animation_Empty
    var order: ViewTransitionOrder = ViewTransitionOrder.Unspecified

    fun enter(
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: ViewTransformationBuilder.() -> Unit
    ) {
        enterDefinition = viewAnimation(interpolator, buildFlow)
    }

    fun enter(
        definition: ViewAnimationDefinition
    ) {
        enterDefinition = definition
    }

    fun exit(
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: ViewTransformationBuilder.() -> Unit
    ) {
        exitDefinition = viewAnimation(interpolator, buildFlow)
    }

    fun exit(
        definition: ViewAnimationDefinition
    ) {
        exitDefinition = definition
    }

    internal fun build(): ViewTransitionDefinition {
        return ViewTransitionDefinition(enterDefinition, exitDefinition, order)
    }

}
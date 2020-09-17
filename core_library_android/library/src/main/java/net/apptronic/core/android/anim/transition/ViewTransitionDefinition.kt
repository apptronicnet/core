package net.apptronic.core.android.anim.transition

import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import net.apptronic.core.android.anim.*
import net.apptronic.core.android.anim.animations.ViewAnimation_Empty

fun viewTransition(
    enterDefinition: ViewAnimationDefinition,
    exitDefinition: ViewAnimationDefinition,
    order: ViewTransitionDirectionSpec = ViewTransitionDirectionSpec.Irrelevant
): ViewTransitionDefinition {
    return viewTransition {
        enter(enterDefinition)
        exit(exitDefinition)
        this.order = order
    }
}

fun viewTransition(
    transitionBuildFlow: TransitionDefinitionBuilder.() -> Unit
): ViewTransitionDefinition {
    val builder = TransitionDefinitionBuilder()
    builder.transitionBuildFlow()
    return builder.build()
}

class ViewTransitionDefinition internal constructor(
    /**
     * Defines animation for entering view when it is on front
     */
    val enterFront: ViewAnimationDefinition,
    /**
     * Defines animation for exiting view when it is on front
     */
    val exitFront: ViewAnimationDefinition,
    /**
     * Defines animation for entering view when it is on back
     */
    val enterBack: ViewAnimationDefinition,
    /**
     * Defines animation for exiting view when it is on back
     */
    val exitBack: ViewAnimationDefinition,
    /**
     * Defines animation for entering view when no exiting view
     */
    val enterSingle: ViewAnimationDefinition,
    /**
     * Defines animation for exiting view when no entering view
     */
    val exitSingle: ViewAnimationDefinition,
    /**
     * Specifies supported orders
     */
    val order: ViewTransitionDirectionSpec
) {

    fun buildTransition(
        enter: View,
        exit: View,
        container: View,
        duration: Long,
        desiredDirection: ViewTransitionDirection?
    ): ViewTransition {
        val resultDesiredDirection = desiredDirection ?: ViewTransitionDirection.EnteringOnFront
        val animationSet = ViewAnimationSet(duration)
        if (resultDesiredDirection == ViewTransitionDirection.EnteringOnFront) {
            animationSet.addAnimation(enterFront, enter, container)
            animationSet.addAnimation(exitBack, exit, container)
        }
        if (resultDesiredDirection == ViewTransitionDirection.ExitingOnFront) {
            animationSet.addAnimation(enterBack, enter, container)
            animationSet.addAnimation(exitFront, exit, container)
        }
        val direction = when (order) {
            ViewTransitionDirectionSpec.EnteringOnFront -> ViewTransitionDirection.EnteringOnFront
            ViewTransitionDirectionSpec.ExitingOnFront -> ViewTransitionDirection.ExitingOnFront
            ViewTransitionDirectionSpec.Bidirectional -> resultDesiredDirection
            ViewTransitionDirectionSpec.Irrelevant -> resultDesiredDirection
        }
        if (resultDesiredDirection != direction) {
            Log.w(
                "ViewTransitionDefinitio",
                "Called buildTransition() with not supported desiredDirection"
            )
        }
        return ViewTransition(animationSet, direction)
    }

    fun buildSingleEnter(target: View, container: View, duration: Long): ViewAnimation {
        return enterSingle.createAnimation(target, container, duration)
    }

    fun buildSingleExit(target: View, container: View, duration: Long): ViewAnimation {
        return exitSingle.createAnimation(target, container, duration)
    }

}

class TransitionDefinitionBuilder internal constructor() {

    var enterFront: ViewAnimationDefinition = ViewAnimation_Empty
    var enterBack: ViewAnimationDefinition = ViewAnimation_Empty
    var exitFront: ViewAnimationDefinition = ViewAnimation_Empty
    var exitBack: ViewAnimationDefinition = ViewAnimation_Empty
    var enterSingle: ViewAnimationDefinition = ViewAnimation_Empty
    var exitSingle: ViewAnimationDefinition = ViewAnimation_Empty
    var order: ViewTransitionDirectionSpec = ViewTransitionDirectionSpec.Irrelevant

    fun enter(
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: ViewTransformationBuilder.() -> Unit
    ) {
        enter(viewAnimation(interpolator, buildFlow))
    }

    fun enter(
        definition: ViewAnimationDefinition
    ) {
        enterFront = definition
        enterBack = definition
        enterSingle = definition
    }

    fun exit(
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: ViewTransformationBuilder.() -> Unit
    ) {
        exit(viewAnimation(interpolator, buildFlow))
    }

    fun exit(
        definition: ViewAnimationDefinition
    ) {
        exitFront = definition
        exitBack = definition
        exitSingle = definition
    }

    internal fun build(): ViewTransitionDefinition {
        return ViewTransitionDefinition(
            enterFront = enterFront,
            exitFront = exitFront,
            enterBack = enterBack,
            exitBack = exitBack,
            enterSingle = enterSingle,
            exitSingle = exitSingle,
            order = order
        )
    }

}
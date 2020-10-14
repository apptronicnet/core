package net.apptronic.core.ios.anim.transition

import net.apptronic.core.ios.anim.ViewAnimation
import net.apptronic.core.ios.anim.ViewAnimationDefinition
import net.apptronic.core.ios.anim.ViewAnimationSet
import net.apptronic.core.ios.anim.animations.ViewAnimation_Empty
import platform.UIKit.UIView

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
            enter: UIView,
            exit: UIView,
            container: UIView,
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
            println("ViewTransitionDefinition: Called buildTransition() with not supported desiredDirection")
        }
        return ViewTransition(animationSet, direction)
    }

    fun buildSingleEnter(target: UIView, container: UIView, duration: Long): ViewAnimation {
        return enterSingle.createAnimation(target, container, duration)
    }

    fun buildSingleExit(target: UIView, container: UIView, duration: Long): ViewAnimation {
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
            definition: ViewAnimationDefinition
    ) {
        enterFront = definition
        enterBack = definition
        enterSingle = definition
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
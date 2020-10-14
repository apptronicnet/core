package net.apptronic.core.ios.anim.factory

import net.apptronic.core.ios.anim.ViewAnimation
import net.apptronic.core.ios.anim.ViewAnimationSet
import net.apptronic.core.ios.anim.animations.ViewAnimation_Empty
import net.apptronic.core.ios.anim.transition.ViewTransition
import net.apptronic.core.ios.anim.transition.ViewTransitionDirection

interface ViewTransitionFactory {

    fun buildViewTransition(spec: ViewTransitionSpec): ViewTransition?

    fun buildSingleEnter(spec: SingleTransitionSpec): ViewAnimation?

    fun buildSingleExit(spec: SingleTransitionSpec): ViewAnimation?

}

fun ViewTransitionFactory.buildViewTransitionOrEmpty(spec: ViewTransitionSpec): ViewTransition {
    return buildViewTransition(spec) ?: ViewTransition(
            ViewAnimationSet(0).also {
                it.addAnimation(ViewAnimation_Empty, spec.enter, spec.container)
                it.addAnimation(ViewAnimation_Empty, spec.exit, spec.container)
            }, spec.direction ?: ViewTransitionDirection.EnteringOnFront
    )
}

fun ViewTransitionFactory.buildSingleEnterOrEmpty(spec: SingleTransitionSpec): ViewAnimation {
    return buildSingleEnter(spec)
            ?: ViewAnimation(spec.target, spec.container, ViewAnimation_Empty, 0)
}

fun ViewTransitionFactory.buildSingleExitOrEmpty(spec: SingleTransitionSpec): ViewAnimation {
    return buildSingleExit(spec)
            ?: ViewAnimation(spec.target, spec.container, ViewAnimation_Empty, 0)
}
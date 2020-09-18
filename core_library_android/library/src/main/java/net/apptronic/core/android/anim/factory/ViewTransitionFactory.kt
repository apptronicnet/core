package net.apptronic.core.android.anim.factory

import net.apptronic.core.android.anim.ViewAnimation
import net.apptronic.core.android.anim.ViewAnimationSet
import net.apptronic.core.android.anim.animations.ViewAnimation_Empty
import net.apptronic.core.android.anim.transition.ViewTransition
import net.apptronic.core.android.anim.transition.ViewTransitionDirection

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
        ?: ViewAnimation_Empty.createAnimation(spec.target, spec.container, 0)
}

fun ViewTransitionFactory.buildSingleExitOrEmpty(spec: SingleTransitionSpec): ViewAnimation {
    return buildSingleExit(spec)
        ?: ViewAnimation_Empty.createAnimation(spec.target, spec.container, 0)
}
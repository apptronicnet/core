package net.apptronic.core.ios.anim.animations

import net.apptronic.core.ios.anim.transition.ViewTransitionDirectionSpec
import net.apptronic.core.ios.anim.transition.viewTransition
import platform.UIKit.UIViewAnimationCurve

const val FORWARD_BACKWARD_OVERLAP = 0.5f
const val MAX_OVERLAY_ALPHA = 0.7f

val Transition_Next = viewTransition {
    enter(ViewAnimation_FromRight(animationCurve = UIViewAnimationCurve.UIViewAnimationCurveEaseInOut))
    exit(ViewAnimation_ToLeft(animationCurve = UIViewAnimationCurve.UIViewAnimationCurveEaseInOut))
}

val Transition_Previous = viewTransition {
    enter(ViewAnimation_FromLeft(animationCurve = UIViewAnimationCurve.UIViewAnimationCurveEaseInOut))
    exit(ViewAnimation_ToRight(animationCurve = UIViewAnimationCurve.UIViewAnimationCurveEaseInOut))
}

val Transition_Forward = viewTransition {
    enter(ViewAnimation_FromRight(animationCurve = UIViewAnimationCurve.UIViewAnimationCurveEaseInOut))
    exit(ViewAnimation_ToLeft(amount = FORWARD_BACKWARD_OVERLAP, animationCurve = UIViewAnimationCurve.UIViewAnimationCurveEaseInOut))
    order = ViewTransitionDirectionSpec.EnteringOnFront
}

val Transition_Backward = viewTransition {
    enter(ViewAnimation_FromLeft(amount = FORWARD_BACKWARD_OVERLAP, animationCurve = UIViewAnimationCurve.UIViewAnimationCurveEaseInOut))
    exit(ViewAnimation_ToRight(animationCurve = UIViewAnimationCurve.UIViewAnimationCurveEaseInOut))
    order = ViewTransitionDirectionSpec.ExitingOnFront
}

val Transition_Fade = viewTransition {
    enterFront = ViewAnimation_FadeIn
    exitFront = ViewAnimation_FadeOut
    enterSingle = ViewAnimation_FadeIn
    exitSingle = ViewAnimation_FadeOut
    order = ViewTransitionDirectionSpec.Bidirectional
}
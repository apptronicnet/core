package net.apptronic.core.android.anim.animations

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.AccelerateDecelerateInterpolator
import net.apptronic.core.android.anim.transformations.*
import net.apptronic.core.android.anim.transition.ViewTransitionOrder
import net.apptronic.core.android.anim.transition.viewTransition

const val FORWARD_BACKWARD_OVERLAP = 0.5f
const val MAX_OVERLAY_ALPHA = 0.7f

val Transition_Next = viewTransition {
    enter(AccelerateDecelerateInterpolator()) {
        translateXToParent(1f, 0f)
    }
    exit(AccelerateDecelerateInterpolator()) {
        translateXToParent(0f, -1f)
    }
}

val Transition_Previous = viewTransition {
    enter(AccelerateDecelerateInterpolator()) {
        translateXToParent(-1f, 0f)
    }
    exit(AccelerateDecelerateInterpolator()) {
        translateXToParent(0f, 1f)
    }
}

val Transition_Forward = viewTransition {
    enter(AccelerateDecelerateInterpolator()) {
        translateXToParent(1f, 0f)
    }
    exit(AccelerateDecelerateInterpolator()) {
        translateXToParent(0f, -FORWARD_BACKWARD_OVERLAP)
        foregroundAlpha(0f, MAX_OVERLAY_ALPHA, ColorDrawable(Color.BLACK))
    }
    order = ViewTransitionOrder.EnteringOnFront
}

val Transition_Backward = viewTransition {
    enter(AccelerateDecelerateInterpolator()) {
        translateXToParent(-FORWARD_BACKWARD_OVERLAP, 0f)
        foregroundAlpha(MAX_OVERLAY_ALPHA, 0f, ColorDrawable(Color.BLACK))
    }
    exit(AccelerateDecelerateInterpolator()) {
        translateXToParent(0f, 1f)
    }
    order = ViewTransitionOrder.ExitingOnFront
}

val Transition_Fade = viewTransition {
    enter(AccelerateDecelerateInterpolator()) {
        alpha(0f, 1f)
    }
    exit(AccelerateDecelerateInterpolator()) {
        if (isIntercepting(
                TranslationZTransformationDescriptor,
                ElevationTransformationDescriptor
            )
        ) {
            alpha(1f, 0f)
        }
    }
    order = ViewTransitionOrder.EnteringOnFront
}
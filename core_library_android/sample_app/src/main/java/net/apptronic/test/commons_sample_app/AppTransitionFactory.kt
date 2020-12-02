package net.apptronic.test.commons_sample_app

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import net.apptronic.core.android.anim.adapter.viewTransitionAdapter
import net.apptronic.core.android.anim.transformations.translateYToSelf
import net.apptronic.core.android.anim.transition.viewTransition
import net.apptronic.test.commons_sample_app.transition.AppTransition

val ViewTransition_SwitchBottomSheet = viewTransition {
    enter(DecelerateInterpolator()) {
        translateYToSelf(1f, 0f)
    }
    exit(AccelerateInterpolator()) {
        translateYToSelf(0f, 1f)
    }
}

val AppTransitionFactory = viewTransitionAdapter {
    bindTransition(AppTransition.BottomSheet, ViewTransition_SwitchBottomSheet)
}
package net.apptronic.core.android.anim.animations

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.AccelerateDecelerateInterpolator
import net.apptronic.core.android.anim.transformations.alpha
import net.apptronic.core.android.anim.transformations.foregroundAlpha
import net.apptronic.core.android.anim.transformations.translateXToParent
import net.apptronic.core.android.anim.viewSwitch

const val FORWARD_BACKWARD_OVERLAP = 0.5f
const val MAX_OVERLAY_ALPHA = 0.7f

val ViewSwitch_Next = viewSwitch {
    enter(AccelerateDecelerateInterpolator()) {
        translateXToParent(1f, 0f)
    }
    exit(AccelerateDecelerateInterpolator()) {
        translateXToParent(0f, -1f)
    }
}

val ViewSwitch_Previous = viewSwitch {
    enter(AccelerateDecelerateInterpolator()) {
        translateXToParent(-1f, 0f)
    }
    exit(AccelerateDecelerateInterpolator()) {
        translateXToParent(0f, 1f)
    }
}

val ViewSwitch_Forward = viewSwitch {
    enter(AccelerateDecelerateInterpolator()) {
        translateXToParent(1f, 0f)
    }
    exit(AccelerateDecelerateInterpolator()) {
        translateXToParent(0f, -FORWARD_BACKWARD_OVERLAP)
        foregroundAlpha(0f, MAX_OVERLAY_ALPHA, ColorDrawable(Color.BLACK))
    }
}

val ViewSwitch_Backward = viewSwitch {
    enter(AccelerateDecelerateInterpolator()) {
        translateXToParent(-FORWARD_BACKWARD_OVERLAP, 0f)
        foregroundAlpha(MAX_OVERLAY_ALPHA, 0f, ColorDrawable(Color.BLACK))
    }
    exit(AccelerateDecelerateInterpolator()) {
        translateXToParent(0f, 1f)
    }
}

val ViewSwitch_Fade = viewSwitch {
    enter(AccelerateDecelerateInterpolator()) {
        alpha(0f, 1f)
    }
    exit(AccelerateDecelerateInterpolator()) {
        alpha(1f, 0f)
    }
}
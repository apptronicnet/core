package net.apptronic.core.android.viewmodel.transitions.siewswitches

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.android.viewmodel.transitions.transformation.foregroundWithAlpha
import net.apptronic.core.android.viewmodel.transitions.transformation.transition
import net.apptronic.core.android.viewmodel.transitions.transformation.translateXToParent

const val FORWARD_BACKWARD_OVERLAP = 0.5f
const val MAX_OVERLAY_ALPHA = 0.7f

fun ViewSwitch.forwardTransition() = transition {
    enter {
        translateXToParent(1f, 0f)
    }
    exit {
        translateXToParent(0f, -FORWARD_BACKWARD_OVERLAP)
        foregroundWithAlpha(0f, MAX_OVERLAY_ALPHA, ColorDrawable(Color.BLACK))
    }
}

fun ViewSwitch.backwardTransition() = transition {
    enter {
        translateXToParent(-FORWARD_BACKWARD_OVERLAP, 0f)
        foregroundWithAlpha(MAX_OVERLAY_ALPHA, 0f, ColorDrawable(Color.BLACK))
    }
    exit {
        translateXToParent(0f, 1f)
    }
}
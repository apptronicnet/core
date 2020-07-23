package net.apptronic.core.android.viewmodel.transitions.viewswitches

import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.android.viewmodel.transitions.transformation.transition
import net.apptronic.core.android.viewmodel.transitions.transformation.translateXToParent

fun ViewSwitch.nextTransition() = transition {
    enter {
        translateXToParent(1f, 0f)
    }
    exit {
        translateXToParent(0f, -1f)
    }
}

fun ViewSwitch.previousTransition() = transition {
    enter {
        translateXToParent(-1f, 0f)
    }
    exit {
        translateXToParent(0f, 1f)
    }
}
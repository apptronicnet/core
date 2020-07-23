package net.apptronic.core.android.viewmodel.transitions.viewswitches

import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.android.viewmodel.transitions.transformation.alpha
import net.apptronic.core.android.viewmodel.transitions.transformation.transition

fun ViewSwitch.fadeTransition() = transition {
    if (exiting == null) {
        enter {
            alpha(0f, 1f)
        }
    } else if (entering == null) {
        exit {
            alpha(1f, 0f)
        }
    } else {
        if (isNewOnFront) {
            enter {
                alpha(0f, 1f)
            }
        } else {
            exit {
                alpha(1f, 0f)
            }
        }
    }
}
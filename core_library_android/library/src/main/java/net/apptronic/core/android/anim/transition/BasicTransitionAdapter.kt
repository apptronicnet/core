package net.apptronic.core.android.anim.transition

import net.apptronic.core.android.anim.animations.*
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition

val BasicTransitionAdapter = viewTransitionAdapter {
    bindTransition(BasicTransition.Fade, Transition_Fade)
    bindTransition(BasicTransition.Previous, Transition_Previous)
    bindTransition(BasicTransition.Next, Transition_Next)
    bindTransition(BasicTransition.Forward, Transition_Forward)
    bindTransition(BasicTransition.Backward, Transition_Backward)
    bindEnterAnimation(BasicTransition.Fade, Animation_Fade)
    bindExitAnimation(BasicTransition.Fade, Animation_Fade.reversed())
}
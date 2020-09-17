package net.apptronic.core.android.anim.adapter

import net.apptronic.core.android.anim.animations.*
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition

val BasicViewTransitionAdapter: ViewTransitionAdapter = viewTransitionAdapter {
    bindTransition(BasicTransition.Fade, Transition_Fade)
    bindTransition(BasicTransition.Previous, Transition_Previous)
    bindTransition(BasicTransition.Next, Transition_Next)
    bindTransition(BasicTransition.Forward, Transition_Forward)
    bindTransition(BasicTransition.Backward, Transition_Backward)
    bindEnterAnimation(BasicTransition.Fade, ViewAnimation_Fade)
    bindExitAnimation(BasicTransition.Fade, ViewAnimation_Fade.reversed())
}
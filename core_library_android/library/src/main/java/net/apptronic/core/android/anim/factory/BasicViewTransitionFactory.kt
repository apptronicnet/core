package net.apptronic.core.android.anim.factory

import net.apptronic.core.android.anim.animations.*
import net.apptronic.core.viewmodel.navigation.BasicTransition

val BasicViewTransitionFactory = viewTransitionFactory {
    bindTransition(BasicTransition.Fade, ViewTransition_Fade)
    bindTransition(BasicTransition.Previous, ViewTransition_Previous)
    bindTransition(BasicTransition.Next, ViewTransition_Next)
    bindTransition(BasicTransition.Forward, ViewTransition_Forward)
    bindTransition(BasicTransition.Backward, ViewTransition_Backward)
    bindEnterAnimation(BasicTransition.Fade, ViewAnimation_Fade)
    bindExitAnimation(BasicTransition.Fade, ViewAnimation_Fade.reversed())
}
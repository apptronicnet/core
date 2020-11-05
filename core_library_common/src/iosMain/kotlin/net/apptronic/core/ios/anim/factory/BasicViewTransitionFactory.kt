package net.apptronic.core.ios.anim.factory

import net.apptronic.core.ios.anim.animations.*
import net.apptronic.core.viewmodel.navigation.BasicTransition

val BasicViewTransitionFactory = viewTransitionFactory {
    bindTransition(BasicTransition.Fade, Transition_Fade)
    bindTransition(BasicTransition.Previous, Transition_Previous)
    bindTransition(BasicTransition.Next, Transition_Next)
    bindTransition(BasicTransition.Forward, Transition_Forward)
    bindTransition(BasicTransition.Backward, Transition_Backward)
    bindEnterAnimation(BasicTransition.Fade, ViewAnimation_FadeIn)
    bindExitAnimation(BasicTransition.Fade, ViewAnimation_FadeOut)
}
package net.apptronic.core.android.viewmodel.transition2

import net.apptronic.core.android.anim.animations.*
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition

val BasicViewSwitchAdapter = viewSwitchAdapter {
    bindViewSwitch(BasicTransition.Fade, ViewSwitch_Fade)
    bindViewSwitch(BasicTransition.Previous, ViewSwitch_Previous)
    bindViewSwitch(BasicTransition.Next, ViewSwitch_Next)
    bindViewSwitch(BasicTransition.Forward, ViewSwitch_Forward)
    bindViewSwitch(BasicTransition.Backward, ViewSwitch_Backward)
}
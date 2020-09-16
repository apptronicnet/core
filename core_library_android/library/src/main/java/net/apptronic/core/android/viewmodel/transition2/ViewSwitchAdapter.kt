package net.apptronic.core.android.viewmodel.transition2

import android.view.View
import net.apptronic.core.android.anim.ViewAnimationSet

interface ViewSwitchAdapter {

    fun buildViewSwitch(
        enter: View?, exit: View?, container: View, duration: Long, transitionSpec: Any?
    ): ViewAnimationSet?

}
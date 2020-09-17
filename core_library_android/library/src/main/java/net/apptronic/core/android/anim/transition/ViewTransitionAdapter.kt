package net.apptronic.core.android.anim.transition

import android.view.View
import net.apptronic.core.android.anim.ViewAnimationSet

interface ViewTransitionAdapter {

    fun buildViewTransition(
        enter: View?, exit: View?, container: View, duration: Long, transitionSpec: Any?
    ): ViewAnimationSet?

    fun getOrder(transitionSpec: Any?): ViewTransitionOrder?

}
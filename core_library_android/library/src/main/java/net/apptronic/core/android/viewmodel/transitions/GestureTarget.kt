package net.apptronic.core.android.viewmodel.transitions

import android.view.View
import net.apptronic.core.mvvm.viewmodel.navigation.BackNavigationStatus

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
interface GestureTarget {

    fun getBackNavigationStatus(): BackNavigationStatus

    fun getFrontView(): View?

    fun getBackView(): View?

    fun onGestureStarted()

    fun onGestureConfirmedPopBackStack()

    fun onGestureCancelled(becauseOfRestricted: Boolean)

}
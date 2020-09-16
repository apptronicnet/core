package net.apptronic.core.android.viewmodel.transition2

import android.view.View
import net.apptronic.core.android.anim.ViewAnimationSet

fun compositeViewSwitchAdapter(vararg adapters: ViewSwitchAdapter?): ViewSwitchAdapter {
    return CompositeViewSwitchAdapter(adapters.filterNotNull())
}

class CompositeViewSwitchAdapter internal constructor(
    private val adapters: List<ViewSwitchAdapter>
) : ViewSwitchAdapter {

    override fun buildViewSwitch(
        enter: View?, exit: View?, container: View, duration: Long, transitionSpec: Any?
    ): ViewAnimationSet? {
        adapters.forEach { adapter ->
            adapter.buildViewSwitch(enter, exit, container, duration, transitionSpec)?.let {
                return it
            }
        }
        return null
    }

}
package net.apptronic.core.android.anim.transition

import android.view.View
import net.apptronic.core.android.anim.ViewAnimationSet

fun compositeViewTransitionAdapter(vararg adapters: ViewTransitionAdapter?): ViewTransitionAdapter {
    return CompositeViewTransitionAdapter(adapters.filterNotNull())
}

private class CompositeViewTransitionAdapter(
    private val adapters: List<ViewTransitionAdapter>
) : ViewTransitionAdapter {

    override fun buildViewTransition(
        enter: View?, exit: View?, container: View, duration: Long, transitionSpec: Any?
    ): ViewAnimationSet? {
        adapters.forEach { adapter ->
            adapter.buildViewTransition(enter, exit, container, duration, transitionSpec)?.let {
                return it
            }
        }
        return null
    }

    override fun getOrder(transitionSpec: Any?): ViewTransitionOrder? {
        adapters.forEach { adapter ->
            adapter.getOrder(transitionSpec)?.let {
                return it
            }
        }
        return null
    }

}
package net.apptronic.core.android.anim.adapter

import net.apptronic.core.android.anim.ViewAnimation
import net.apptronic.core.android.anim.transition.ViewTransition

fun compositeViewTransitionAdapter(vararg adapters: ViewTransitionAdapter?): ViewTransitionAdapter {
    return CompositeViewTransitionAdapter(adapters.filterNotNull())
}

private class CompositeViewTransitionAdapter(
    private val adapters: List<ViewTransitionAdapter>
) : ViewTransitionAdapter {

    override fun buildViewTransition(spec: ViewTransitionSpec): ViewTransition? {
        adapters.forEach { adapter ->
            adapter.buildViewTransition(spec)?.let {
                return it
            }
        }
        return null
    }

    override fun buildSingleEnter(spec: SingleTransitionSpec): ViewAnimation? {
        adapters.forEach { adapter ->
            adapter.buildSingleEnter(spec)?.let {
                return it
            }
        }
        return null
    }

    override fun buildSingleExit(spec: SingleTransitionSpec): ViewAnimation? {
        adapters.forEach { adapter ->
            adapter.buildSingleExit(spec)?.let {
                return it
            }
        }
        return null
    }

}
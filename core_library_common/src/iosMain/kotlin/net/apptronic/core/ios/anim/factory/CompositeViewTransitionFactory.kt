package net.apptronic.core.ios.anim.factory

import net.apptronic.core.ios.anim.ViewAnimation
import net.apptronic.core.ios.anim.transition.ViewTransition

fun compositeViewTransitionFactory(vararg factories: ViewTransitionFactory?): ViewTransitionFactory {
    return CompositeViewTransitionFactory(factories.filterNotNull())
}

private class CompositeViewTransitionFactory(
        private val factories: List<ViewTransitionFactory>
) : ViewTransitionFactory {

    override fun buildViewTransition(spec: ViewTransitionSpec): ViewTransition? {
        factories.forEach { adapter ->
            adapter.buildViewTransition(spec)?.let {
                return it
            }
        }
        return null
    }

    override fun buildSingleEnter(spec: SingleTransitionSpec): ViewAnimation? {
        factories.forEach { adapter ->
            adapter.buildSingleEnter(spec)?.let {
                return it
            }
        }
        return null
    }

    override fun buildSingleExit(spec: SingleTransitionSpec): ViewAnimation? {
        factories.forEach { adapter ->
            adapter.buildSingleExit(spec)?.let {
                return it
            }
        }
        return null
    }

}
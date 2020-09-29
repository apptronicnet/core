package net.apptronic.core.view.containers

import net.apptronic.core.view.CoreViewBuilder

/**
 * View container in which all views are placed one over another
 */
open class FrameCoreContainerView internal constructor() : CoreContainerView()

fun CoreViewBuilder.frameContainer(builder: FrameCoreContainerView.() -> Unit): FrameCoreContainerView {
    return onNextView(FrameCoreContainerView(), builder)
}
package net.apptronic.core.view.containers

import net.apptronic.core.view.ICoreViewBuilder

/**
 * View container in which all views are placed one over another
 */
open class FrameCoreContainerView internal constructor() : CoreContainerView()

fun ICoreViewBuilder.frameContainer(builder: FrameCoreContainerView.() -> Unit): FrameCoreContainerView {
    return onNextView(FrameCoreContainerView(), builder)
}
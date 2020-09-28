package net.apptronic.core.view.container

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext

/**
 * View container in which all views are placed one over another
 */
open class FrameCoreContainerView internal constructor(
        context: CoreViewContext
) : CoreContainerView(context)

fun CoreViewBuilder.frameContainer(builder: FrameCoreContainerView.() -> Unit): FrameCoreContainerView {
    return nextView(::FrameCoreContainerView, builder)
}
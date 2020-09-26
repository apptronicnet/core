package net.apptronic.core.view.container

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration

/**
 * View container in which all views are placed one over another
 */
open class FrameCoreContainerView internal constructor(
        viewConfiguration: ViewConfiguration
) : BaseCoreContainerView(viewConfiguration)

fun CoreViewBuilder.frameContainer(builder: FrameCoreContainerView.() -> Unit) {
    nextView(FrameCoreContainerView(viewConfiguration), builder)
}
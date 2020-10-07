package net.apptronic.core.view.containers

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.view.ICoreViewBuilder

/**
 * View container in which all views are placed one over another
 */
@UnderDevelopment
open class CoreFrameContainerView internal constructor() : CoreContainerView()

@UnderDevelopment
fun ICoreViewBuilder.frameContainer(builder: CoreFrameContainerView.() -> Unit): CoreFrameContainerView {
    return onNextView(CoreFrameContainerView(), builder)
}
package net.apptronic.core.view.container

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration

open class FrameCoreContainerView internal constructor(
        viewConfiguration: ViewConfiguration
) : BaseCoreContainerView(viewConfiguration) {

}

fun CoreViewBuilder.frameView(builder: FrameCoreContainerView.() -> Unit) {
    nextView(FrameCoreContainerView(viewConfiguration).also(builder))
}
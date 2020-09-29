package net.apptronic.core.view.containers

import net.apptronic.core.view.CoreViewBuilder
import net.apptronic.core.view.ICoreContentView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty

/**
 * Base interface for view container which can hold other views.
 */
interface ICoreContainerView : ICoreContentView, CoreViewBuilder {

    val children: ViewProperty<List<ICoreView>>

}
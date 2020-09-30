package net.apptronic.core.view.containers

import net.apptronic.core.view.*

/**
 * Base interface for view container which can hold other views.
 */
interface ICoreContainerView : ICoreContentView, ICoreViewBuilder, ICoreParentView {

    val children: ViewProperty<List<ICoreView>>

}
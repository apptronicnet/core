package net.apptronic.core.view.container

import net.apptronic.core.view.CoreContentView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.CoreViewBuilder

/**
 * Base interface for view container which can hold other views.
 */
interface ICoreContainerView : CoreContentView, CoreViewBuilder {

    fun getChildren(): List<ICoreView>

}
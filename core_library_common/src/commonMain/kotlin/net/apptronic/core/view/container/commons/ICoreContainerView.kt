package net.apptronic.core.view.container.commons

import net.apptronic.core.view.CoreContentView
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder

/**
 * Base interface for view container which can hold other views.
 */
interface ICoreContainerView : CoreView, CoreContentView, CoreViewBuilder {

    fun getChildren(): List<CoreView>

}
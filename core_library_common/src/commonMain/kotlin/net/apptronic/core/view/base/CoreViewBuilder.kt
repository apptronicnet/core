package net.apptronic.core.view.base

import net.apptronic.core.view.CoreView

/**
 * Base class for objects which can create views
 */
interface CoreViewBuilder : CoreViewBase {

    fun <T : CoreView> nextView(child: T, builder: T.() -> Unit)

}
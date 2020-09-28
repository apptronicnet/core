package net.apptronic.core.view.base

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.context.CoreViewContext

/**
 * Base class for objects which can create views
 */
interface CoreViewBuilder : CoreViewBase {

    fun <T : ICoreView> nextView(constructor: (CoreViewContext) -> T, builder: T.() -> Unit = {}): T {
        val child = constructor(createChildCoreViewContext())
        context.applyThemes(child)
        child.builder()
        return child
    }

}
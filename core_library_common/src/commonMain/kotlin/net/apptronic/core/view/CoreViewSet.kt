package net.apptronic.core.view

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext

/**
 * Class which can create and hold multiple views but not renders them, allowing them to be used by someone else
 */
class CoreViewSet(override val context: CoreViewContext) : CoreViewBuilder {

    private var views = mutableListOf<ICoreView>()

    override fun <T : ICoreView> nextView(constructor: (CoreViewContext) -> T, builder: T.() -> Unit): T {
        return super.nextView(constructor, builder).also {
            views.add(it)
        }
    }


    fun getViews(): List<ICoreView> {
        return views
    }

}
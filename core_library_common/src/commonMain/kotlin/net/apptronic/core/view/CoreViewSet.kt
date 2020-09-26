package net.apptronic.core.view

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration

class CoreViewSet(override val viewConfiguration: ViewConfiguration) : CoreViewBuilder {

    private var isRecycledState = false

    override val isRecycled: Boolean
        get() = isRecycledState

    private var views = mutableListOf<CoreView>()

    override fun nextView(child: CoreView) {
        if (!isRecycled) {
            views.add(child)
        } else {
            child.recycle()
        }
    }

    fun getViews(): List<CoreView> {
        return views
    }

    override fun recycle() {
        views.forEach {
            it.recycle()
        }
        views.clear()
        isRecycledState = true
    }

}
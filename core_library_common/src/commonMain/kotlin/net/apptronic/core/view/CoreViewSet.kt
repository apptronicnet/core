package net.apptronic.core.view

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration

/**
 * Class which can create and hold multiple views but not renders them, allowing them to be used by someone else
 */
class CoreViewSet(override val viewConfiguration: ViewConfiguration) : CoreViewBuilder {

    private var isRecycledState = false

    override val isRecycled: Boolean
        get() = isRecycledState

    private var views = mutableListOf<CoreView>()

    override fun <T : CoreView> nextView(child: T, builder: T.() -> Unit) {
        if (!isRecycled) {
            child.recycle()
            return
        }
        child.builder()
        views.add(child)
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
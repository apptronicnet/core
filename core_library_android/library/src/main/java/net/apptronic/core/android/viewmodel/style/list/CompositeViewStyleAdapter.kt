package net.apptronic.core.android.viewmodel.style.list

import android.view.View
import net.apptronic.core.viewmodel.IViewModel

fun compositeViewStyleAdapter(
    vararg adapters: ViewStyleAdapter
): ViewStyleAdapter {
    return CompositeViewStyleAdapter(adapters)
}

fun compositeViewStyleAdapter(
    adapters: List<ViewStyleAdapter>
): ViewStyleAdapter {
    return CompositeViewStyleAdapter(adapters.toTypedArray())
}

private class CompositeViewStyleAdapter(
    private val adapters: Array<out ViewStyleAdapter>
) : ViewStyleAdapter {

    override fun applyViewStyle(view: View, position: Int, list: List<IViewModel>) {
        adapters.forEach {
            it.applyViewStyle(view, position, list)
        }
    }

}
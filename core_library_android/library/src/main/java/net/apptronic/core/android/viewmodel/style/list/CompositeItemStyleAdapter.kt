package net.apptronic.core.android.viewmodel.style.list

import android.view.View
import net.apptronic.core.mvvm.viewmodel.IViewModel

fun compositeItemStyleAdapter(
    vararg adapters: ListItemStyleAdapter
): ListItemStyleAdapter {
    return CompositeItemStyleAdapter(adapters)
}

fun compositeItemStyleAdapter(
    adapters: List<ListItemStyleAdapter>
): ListItemStyleAdapter {
    return CompositeItemStyleAdapter(adapters.toTypedArray())
}

private class CompositeItemStyleAdapter(
    private val adapters: Array<out ListItemStyleAdapter>
) : ListItemStyleAdapter {

    override fun applyViewStyle(view: View, position: Int, list: List<IViewModel>) {
        adapters.forEach {
            it.applyViewStyle(view, position, list)
        }
    }

}
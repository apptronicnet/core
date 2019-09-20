package net.apptronic.core.android.viewmodel.style.list

import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel

internal fun emptyStyleAdapter(): ListItemStyleAdapter {
    return EmptyListItemStyleAdapter()
}

private class EmptyListItemStyleAdapter :
    ListItemStyleAdapter {

    override fun applyViewStyle(view: View, position: Int, list: List<ViewModel>) {
        // do nothing
    }

}
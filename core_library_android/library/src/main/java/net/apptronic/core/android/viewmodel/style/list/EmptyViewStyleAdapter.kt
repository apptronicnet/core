package net.apptronic.core.android.viewmodel.style.list

import android.view.View
import net.apptronic.core.mvvm.viewmodel.IViewModel

internal fun emptyViewStyleAdapter(): ViewStyleAdapter {
    return EmptyViewStyleAdapter()
}

private class EmptyViewStyleAdapter :
    ViewStyleAdapter {

    override fun applyViewStyle(view: View, position: Int, list: List<IViewModel>) {
        // do nothing
    }

}
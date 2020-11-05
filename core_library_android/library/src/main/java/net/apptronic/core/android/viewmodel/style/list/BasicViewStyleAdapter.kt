package net.apptronic.core.android.viewmodel.style.list

import android.view.View
import net.apptronic.core.viewmodel.IViewModel

interface BasicViewStyleAdapter : ViewStyleAdapter {

    override fun applyViewStyle(view: View, position: Int, list: List<IViewModel>) {
        val previous = list.getOrNull(position - 1)
        val current = list[position]
        val next = list.getOrNull(position + 1)
        applyViewStyle(view, previous, current, next)
    }

    fun applyViewStyle(
        view: View, previous: IViewModel?, current: IViewModel, next: IViewModel?
    )

}
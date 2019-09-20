package net.apptronic.core.android.viewmodel.style.list

import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel

interface BasicListItemStyleAdapter : ListItemStyleAdapter {

    override fun applyViewStyle(view: View, position: Int, list: List<ViewModel>) {
        val previous = list.getOrNull(position - 1)
        val current = list[position]
        val next = list.getOrNull(position + 1)
        applyViewStyle(view, previous, current, next)
    }

    fun applyViewStyle(
        view: View, previous: ViewModel?, current: ViewModel, next: ViewModel?
    )

}
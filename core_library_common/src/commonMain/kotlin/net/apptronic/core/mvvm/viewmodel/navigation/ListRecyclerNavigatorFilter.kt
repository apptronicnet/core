package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel

class ListItem(
        val item: Any,
        val viewModel: ViewModel?,
        val isVisible: Boolean,
        val isStatic: Boolean
)

interface ListRecyclerNavigatorFilter {

    fun filter(items: List<ListItem>): RecyclerListIndexMapping

}

interface RecyclerListIndexMapping {

    fun getSize(): Int

    fun mapIndex(sourceIndex: Int): Int

}
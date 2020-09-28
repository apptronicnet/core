package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Item which is provided to list in [ListRecyclerNavigatorFilter.filter]
 */
class ListItem(
        /**
         * Source list item used for [DynamicListNavigator]
         */
        val item: Any,
        /**
         * Currently assigned [ViewModel] if exists at this moment.
         */
        val viewModel: IViewModel?,
        /**
         * Defines value returned by [VisibilityFilter] for [ViewModel]. Works only for static items. For dynamic items
         * always returns true.
         */
        val isVisible: Boolean,
        /**
         * Defines is item is static.
         */
        val isStatic: Boolean
)

/**
 * This class applies filtering for [DynamicListNavigator]. In comparison with [ListNavigatorFilter] it does not
 * checks whole list as it may have huge size and take too much time to check. Instead of changes list if provides
 * [DynamicListIndexMapping] for source list transformation dynamically when each concrete item is requested.
 */
interface ListRecyclerNavigatorFilter {

    /**
     * Perform [DynamicListNavigator] source items list filtering. Please note that this is is lazy meaning each item
     * allocating at the moment if request. It allows to safely filter only part of list without no need to check whole
     * list at same time.
     * @param lazy-allocated list of items
     * @param listDescription optional value set by [DynamicListNavigator.set] to help with list filtering and mapping
     */
    fun filter(items: List<ListItem>, listDescription: Any?): DynamicListIndexMapping

}

/**
 * This is class for dynamic allocation of size and indexes for list of items contained in [DynamicListNavigator].
 * As source items list may be very big it is impossible to filter whole list. Instead of that filtering are performed
 * dynamically by remapping of indexes and list size by instance of this class.
 */
interface DynamicListIndexMapping {

    fun getSize(): Int

    fun mapIndex(sourceIndex: Int): Int

}
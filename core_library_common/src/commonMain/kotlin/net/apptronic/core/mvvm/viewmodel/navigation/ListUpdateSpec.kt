package net.apptronic.core.mvvm.viewmodel.navigation

/**
 * Standard specs for changes inside of [ListNavigator]
 */
sealed class ListUpdateSpec {

    /**
     * Single item added to list at index [index]
     */
    class ItemAdded(val index: Int) : ListUpdateSpec()

    /**
     * Single item removed from list at index [index]
     */
    class ItemRemoved(val index: Int) : ListUpdateSpec()

    /**
     * Single item moved from inside list fr0m index [fromIndex] to index [:]
     */
    class ItemMoved(val fromIndex: Int, val toIndex: Int) : ListUpdateSpec()

    /**
     * New items inserted to list at [range]
     */
    class RangeInserted(val range: IntRange) : ListUpdateSpec()

    /**
     * Items deleted from list at [range]
     */
    class RangeRemoved(val range: IntRange) : ListUpdateSpec()

}

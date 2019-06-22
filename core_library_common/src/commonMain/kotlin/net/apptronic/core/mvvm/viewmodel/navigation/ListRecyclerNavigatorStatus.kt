package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing current status of [ListRecyclerNavigator]
 */
class ListRecyclerNavigatorStatus(
        /**
         * Size of all items in navigator
         */
        val allSize: Int,
        /**
         * Size of visible items in navigator
         */
        val visibleSize: Int,
        /**
         * Defines is navigator have hidden items
         */
        val hasHidden: Boolean,
        /**
         * List of all items
         */
        val allItems: List<Any>,
        /**
         * List of visible items
         */
        val visibleItems: List<Any>,
        /**
         * List of static items
         */
        val staticItems: List<Any>,
        /**
         * Set of currently attached [ViewModel]s
         */
        val attachedViewModels: Set<ViewModel>
)
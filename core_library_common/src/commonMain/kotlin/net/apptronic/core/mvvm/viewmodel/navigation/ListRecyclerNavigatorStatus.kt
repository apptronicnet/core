package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing current status of [DynamicListNavigator]
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
        val attachedViewModels: Set<IViewModel>
)

fun Entity<ListRecyclerNavigatorStatus>.allSize(): Entity<Int> {
        return map { it.allSize }
}

fun Entity<ListRecyclerNavigatorStatus>.visibleSize(): Entity<Int> {
        return map { it.visibleSize }
}

fun Entity<ListRecyclerNavigatorStatus>.hasHidden(): Entity<Boolean> {
        return map { it.hasHidden }
}

fun Entity<ListRecyclerNavigatorStatus>.allItems(): Entity<List<Any>> {
        return map { it.allItems }
}

fun Entity<ListRecyclerNavigatorStatus>.visibleItems(): Entity<List<Any>> {
        return map { it.visibleItems }
}

fun Entity<ListRecyclerNavigatorStatus>.staticItems(): Entity<List<Any>> {
        return map { it.staticItems }
}

fun Entity<ListRecyclerNavigatorStatus>.attachedViewModels(): Entity<Set<IViewModel>> {
        return map { it.attachedViewModels }
}


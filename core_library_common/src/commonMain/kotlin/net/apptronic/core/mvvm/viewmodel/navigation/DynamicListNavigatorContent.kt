package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing current status of [DynamicListNavigator]
 */
class DynamicListNavigatorStatus<T, State>(
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
        val allItems: List<T>,
        /**
         * List of visible items
         */
        val visibleItems: List<T>,
        /**
         * List of static items
         */
        val staticItems: List<T>,
        /**
         * Set of currently attached [ViewModel]s
         */
        val attachedViewModels: Set<IViewModel>,
        val state: State
)

fun Entity<DynamicListNavigatorStatus<*, *>>.allSize(): Entity<Int> {
    return map { it.allSize }
}

fun Entity<DynamicListNavigatorStatus<*, *>>.visibleSize(): Entity<Int> {
    return map { it.visibleSize }
}

fun Entity<DynamicListNavigatorStatus<*, *>>.hasHidden(): Entity<Boolean> {
    return map { it.hasHidden }
}

fun <T> Entity<DynamicListNavigatorStatus<T, *>>.allItems(): Entity<List<T>> {
    return map { it.allItems }
}

fun <T> Entity<DynamicListNavigatorStatus<T, *>>.visibleItems(): Entity<List<T>> {
    return map { it.visibleItems }
}

fun <T> Entity<DynamicListNavigatorStatus<T, *>>.staticItems(): Entity<List<T>> {
    return map { it.staticItems }
}

fun Entity<DynamicListNavigatorStatus<*, *>>.attachedViewModels(): Entity<Set<IViewModel>> {
    return map { it.attachedViewModels }
}

fun <State> Entity<DynamicListNavigatorStatus<*, State>>.state(): Entity<State> {
    return map { it.state }
}


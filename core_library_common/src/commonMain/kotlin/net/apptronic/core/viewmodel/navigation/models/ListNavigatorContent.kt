package net.apptronic.core.viewmodel.navigation.models

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.functions.map
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel

interface ListNavigatorContent<T, State> {
    val all: List<T>
    val visible: List<T>
    val countAll: Int
    val countVisible: Int
    val countHidden: Int
    val state: State
    val hasHidden: Boolean
}

fun <T> Entity<out ListNavigatorContent<T, *>>.all(): Entity<List<T>> {
    return map { it.all }
}

fun <T> Entity<out ListNavigatorContent<T, *>>.visible(): Entity<List<T>> {
    return map { it.visible }
}

fun Entity<out ListNavigatorContent<*, *>>.countVisible(): Entity<Int> {
    return map { it.countVisible }
}

fun Entity<out ListNavigatorContent<*, *>>.countAll(): Entity<Int> {
    return map { it.countAll }
}

fun Entity<out ListNavigatorContent<*, *>>.countHidden(): Entity<Int> {
    return map { it.countHidden }
}

fun Entity<out ListNavigatorContent<*, *>>.hasHidden(): Entity<Boolean> {
    return map { it.hasHidden }
}

fun <State> Entity<out ListNavigatorContent<*, State>>.state(): Entity<State> {
    return map { it.state }
}

/**
 * Class representing current status of [StaticListNavigator]
 */
class StaticListNavigatorContent<State> internal constructor(
        /**
         * All [ViewModel]s which now in [StaticListNavigator]
         */
        override val all: List<IViewModel>,
        /**
         * Visible [ViewModel]s which now in [StaticListNavigator] and ready to be bound to view
         */
        override val visible: List<IViewModel>,
        /**
         * State of navigator
         */
        override val state: State
) : ListNavigatorContent<IViewModel, State> {

    /**
     * Count of all items in list
     */
    override val countAll: Int = all.size

    /**
     * Count if visible items in list
     */
    override val countVisible: Int = visible.size

    /**
     * Count of hidden items in list
     */
    override val countHidden: Int = countAll - countVisible

    /**
     * Is list has hidden items
     */
    override val hasHidden: Boolean = countHidden > 0

}

/**
 * Class representing current status of [DynamicListNavigator]
 */
class DynamicListNavigatorContent<T, State>(
        /**
         * Size of all items in navigator
         */
        override val countAll: Int,
        /**
         * Size of visible items in navigator
         */
        override val countVisible: Int,
        /**
         * Defines is navigator have hidden items
         */
        override val hasHidden: Boolean,
        /**
         * List of all items
         */
        override val all: List<T>,
        /**
         * List of visible items
         */
        override val visible: List<T>,
        /**
         * List of static items
         */
        val staticItems: List<T>,
        /**
         * Set of currently attached [ViewModel]s
         */
        val attachedViewModels: Set<IViewModel>,
        /**
         * State of navigator
         */
        override val state: State
) : ListNavigatorContent<T, State> {
    override val countHidden: Int = countAll - countVisible
}

fun <T> Entity<out DynamicListNavigatorContent<T, *>>.staticItems(): Entity<List<T>> {
    return map { it.staticItems }
}

fun Entity<out DynamicListNavigatorContent<*, *>>.attachedViewModels(): Entity<Set<IViewModel>> {
    return map { it.attachedViewModels }
}
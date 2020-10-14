package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing current status of [StaticListNavigator]
 */
class ListNavigatorContent<State> internal constructor(
        /**
         * All [ViewModel]s which now in [StaticListNavigator]
         */
        val all: List<IViewModel>,
        /**
         * Visible [ViewModel]s which now in [StaticListNavigator] and ready to be bound to view
         */
        val visible: List<IViewModel>,
        val state: State
) {

    /**
     * Count of all items in list
     */
    val countAll: Int = all.size

    /**
     * Count if visible items in list
     */
    val countVisible: Int = visible.size

    /**
     * Count of hidden items in list
     */
    val countHidden: Int = countAll - countVisible

    /**
     * Is list has hidden items
     */
    val hasHidden: Boolean = countHidden > 0

}

fun Entity<ListNavigatorContent<*>>.all(): Entity<List<IViewModel>> {
    return map { it.all }
}

fun Entity<ListNavigatorContent<*>>.visible(): Entity<List<IViewModel>> {
    return map { it.visible }
}

fun Entity<ListNavigatorContent<*>>.countVisible(): Entity<Int> {
    return map { it.countVisible }
}

fun Entity<ListNavigatorContent<*>>.countAll(): Entity<Int> {
    return map { it.countAll }
}

fun Entity<ListNavigatorContent<*>>.countHidden(): Entity<Int> {
    return map { it.countHidden }
}

fun Entity<ListNavigatorContent<*>>.hasHidden(): Entity<Boolean> {
    return map { it.hasHidden }
}

fun <State> Entity<ListNavigatorContent<State>>.state(): Entity<State> {
    return map { it.state }
}
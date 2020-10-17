package net.apptronic.core.mvvm.viewmodel.navigation.models

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing status of [IStackNavigationModel]
 */
data class SingleItemNavigatorContent internal constructor(
        /**
         * Index of current actual [ViewModel] which should be visible ti user
         */
        val visibleIndex: Int,
        /**
         * Current list of [ViewModel]s
         */
        val items: List<IViewModel>
) {

    /**
     * Current actual [ViewModel] which should be visible ti user
     */
    val viewModel: IViewModel?
        get() {
            return items.getOrNull(visibleIndex)
        }

    /**
     * Current count of [ViewModel]s inside
     */
    val size: Int = items.size

    private fun deepEquals(other: SingleItemNavigatorContent): Boolean {
        return visibleIndex == other.visibleIndex
                && items.size == other.items.size
                && items.toTypedArray().contentEquals(other.items.toTypedArray())
    }

    override fun equals(other: Any?): Boolean {
        return other is SingleItemNavigatorContent && deepEquals(other)
    }

    override fun hashCode(): Int {
        return viewModel.hashCode() xor size xor (Int.MAX_VALUE - items.size)
    }

}

fun Entity<SingleItemNavigatorContent>.viewModel(): Entity<IViewModel?> {
    return map { it.viewModel }
}

fun Entity<SingleItemNavigatorContent>.size(): Entity<Int> {
    return map { it.size }
}

fun Entity<SingleItemNavigatorContent>.stack(): Entity<List<IViewModel>?> {
    return map { it.items }
}
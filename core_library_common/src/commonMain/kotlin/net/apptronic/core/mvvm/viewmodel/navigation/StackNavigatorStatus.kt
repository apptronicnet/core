package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing status of [StackNavigator]
 */
data class StackNavigatorStatus internal constructor(
        /**
         * Defines in some [ViewModel] are loading and pending to be shown
         */
        val isInProgress: Boolean,
        /**
         * Current actual [ViewModel] which represents end of navigation
         */
        val actualModel: IViewModel?,
        /**
         * Current visible [ViewModel]. Same as [actualModel] when no progress but may be different pr null
         * if new [ViewModel] is loading's it's data inside [StackNavigator] and waiting to be shown
         */
        val visibleModel: IViewModel?,
        /**
         * Current size of stack
         */
        val size: Int,
        /**
         * Current stack of [ViewModel]s
         */
        val stack: List<IViewModel>
) {

    internal fun deepEquals(other: StackNavigatorStatus): Boolean {
        return isInProgress == other.isInProgress
                && actualModel == other.actualModel
                && visibleModel == other.visibleModel
                && size == other.size
                && stack.size == other.stack.size
                && stack.toTypedArray().contentEquals(other.stack.toTypedArray())
    }

}

fun Entity<StackNavigatorStatus>.progress(): Entity<Boolean> {
    return map { it.isInProgress }
}


fun Entity<StackNavigatorStatus>.actualModel(): Entity<IViewModel?> {
    return map { it.actualModel }
}

fun Entity<StackNavigatorStatus>.visibleModel(): Entity<IViewModel?> {
    return map { it.visibleModel }
}


fun Entity<StackNavigatorStatus>.size(): Entity<Int> {
    return map { it.size }
}


fun Entity<StackNavigatorStatus>.stack(): Entity<List<IViewModel>?> {
    return map { it.stack }
}
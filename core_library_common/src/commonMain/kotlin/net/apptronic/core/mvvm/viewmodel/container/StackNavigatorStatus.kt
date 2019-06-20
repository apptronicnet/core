package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing status of [ViewModelStackNavigator]
 */
data class StackNavigatorStatus(
        /**
         * Defines in some [ViewModel] are loading and pending to be shown
         */
        val isInProgress: Boolean,
        /**
         * Current actual [ViewModel] which represents end of navigation
         */
        val actualModel: ViewModel?,
        /**
         * Current visible [ViewModel]. Same as [actualModel] when no progress but may be different pr null
         * if new [ViewModel] is loading's it's data inside [ViewModelStackNavigator] and waiting to be shown
         */
        val visibleModel: ViewModel?
)

fun Entity<StackNavigatorStatus>.progress(): Entity<Boolean> {
    return map { it.isInProgress }
}


fun Entity<StackNavigatorStatus>.takeActualModel(): Entity<ViewModel?> {
    return map { it.actualModel }
}

fun Entity<StackNavigatorStatus>.takeVisibleModel(): Entity<ViewModel?> {
    return map { it.visibleModel }
}
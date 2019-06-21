package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing current status of [ListNavigator]
 */
class ListNavigatorStatus internal constructor(
        /**
         * All [ViewModel]s which now in [ListNavigator]
         */
        val all: List<ViewModel>,
        /**
         * Visible [ViewModel]s which now in [ListNavigator] and ready to be bound to view
         */
        val visible: List<ViewModel>
)
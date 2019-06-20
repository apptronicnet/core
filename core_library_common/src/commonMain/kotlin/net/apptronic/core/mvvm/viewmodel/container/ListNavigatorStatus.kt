package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class representing current status of [ViewModelListNavigator]
 */
class ListNavigatorStatus(
        /**
         * All [ViewModel]s which now in [ViewModelListNavigator]
         */
        val all: List<ViewModel>,
        /**
         * Visible [ViewModel]s which now in [ViewModelListNavigator] and ready to be bound to view
         */
        val visible: List<ViewModel>
)
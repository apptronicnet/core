package net.apptronic.core.viewmodel.navigation.adapters

import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.TransitionInfo
import net.apptronic.core.viewmodel.navigation.ViewModelItem

/**
 * View model adapter which implements ability to show single [ViewModel] but acces to all other
 * [ViewModel]s in navigator no cache/precreate views fot it
 */
interface SingleViewModelListAdapter {

    /**
     * Called when state is changed: items list changed or visible index changed
     */
    fun onInvalidate(items: List<ViewModelItem>, visibleIndex: Int, transitionInfo: TransitionInfo)

}
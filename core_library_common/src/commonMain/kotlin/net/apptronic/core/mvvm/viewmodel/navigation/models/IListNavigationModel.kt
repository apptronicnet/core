package net.apptronic.core.mvvm.viewmodel.navigation.models

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.INavigator

/**
 * Navigation model which manages list of [ViewModel]s and allows to display any number of that at same time
 */
interface IListNavigationModel<Content : ListNavigatorContent<T, State>, T, State> : INavigator<Content>, SupportsViewModelListAdapter<State> {

    val size: Int

    val state: State

    fun getItemAt(index: Int): T

    fun getViewModelAt(index: Int): IViewModel

    fun set(items: List<T>, state: State, updateSpec: Any? = null)

    fun updateState(state: State, updateSpec: Any? = null)

    fun getItems(): List<T>

    fun getViewModels(): List<IViewModel>

}
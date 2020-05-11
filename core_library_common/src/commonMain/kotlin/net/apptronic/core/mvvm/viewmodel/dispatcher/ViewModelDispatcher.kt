package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.mvvm.viewmodel.ViewModel

interface ViewModelDispatcher<T : ViewModel> {

    fun getViewModel(): T

    fun registerContainer(container: UiContainer<T>)

    fun unregisterContainer(container: UiContainer<T>)

    fun recycleViewModel()

}
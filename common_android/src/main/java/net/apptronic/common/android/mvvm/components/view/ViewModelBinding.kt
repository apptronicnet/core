package net.apptronic.common.android.mvvm.components.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.common.core.component.Component

abstract class ViewModelBinding<T : Component>(
    val viewModel: Component
) {

    abstract fun onCreateLayout(layoutInflater: LayoutInflater, container: ViewGroup)

    abstract fun onBindLayout(layout: View, viewModel: T)

    abstract fun onUnbindLayout()

}
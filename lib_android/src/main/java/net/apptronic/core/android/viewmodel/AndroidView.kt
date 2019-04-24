package net.apptronic.core.android.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class AndroidView<T : ViewModel>(viewModel: T) : BaseAndroidView<T>(
    viewModel
) {

    internal fun bindView(container: ViewGroup) {
        val inflater = LayoutInflater.from(container.context)
        contentView = onCreateView(inflater, container)
        onBindView()
    }

    abstract fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View

}
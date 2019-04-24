package net.apptronic.core.android.viewmodel

import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class AndroidActivityView<T : ViewModel>(
    viewModel: T
) : BaseAndroidView<T>(viewModel) {

    internal fun bindView(contentView: View) {
        this.contentView = contentView
        onBindView()
    }

}
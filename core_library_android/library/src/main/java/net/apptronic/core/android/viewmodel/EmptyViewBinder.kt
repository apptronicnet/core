package net.apptronic.core.android.viewmodel

import net.apptronic.core.mvvm.viewmodel.IViewModel

open class EmptyViewBinder<T : IViewModel> : ViewBinder<T>() {

    override fun onBindView() {
        // ignore
    }

}
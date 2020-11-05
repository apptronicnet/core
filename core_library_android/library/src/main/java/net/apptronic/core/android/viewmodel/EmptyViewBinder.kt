package net.apptronic.core.android.viewmodel

import net.apptronic.core.viewmodel.IViewModel

open class EmptyViewBinder<T : IViewModel> : ViewBinder<T>() {

    override fun onBindView() {
        // ignore
    }

}
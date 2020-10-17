package net.apptronic.core.android.viewmodel

import android.view.View
import android.widget.TextView
import net.apptronic.core.mvvm.viewmodel.IViewModel

class GenericViewBinder<T : IViewModel> : ViewBinder<T>() {

    override fun onBindView(view: View, viewModel: T) {
        (view as? TextView)?.text = viewModel::class.simpleName ?: "ViewModel"
    }

}
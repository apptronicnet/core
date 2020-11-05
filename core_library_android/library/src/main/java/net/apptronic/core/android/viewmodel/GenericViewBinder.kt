package net.apptronic.core.android.viewmodel

import android.widget.TextView
import net.apptronic.core.viewmodel.IViewModel

internal class GenericViewBinder<T : IViewModel> : ViewBinder<T>() {

    override fun onBindView() {
        (view as? TextView)?.text = viewModel::class.simpleName ?: "ViewModel"
    }

}
package net.apptronic.core.android.viewmodel

import android.view.View
import android.widget.TextView
import net.apptronic.core.mvvm.viewmodel.IViewModel

class GenericViewBinder : ViewBinder<IViewModel>() {

    override fun onBindView(view: View, viewModel: IViewModel) {
        (view as? TextView)?.text = viewModel::class.simpleName ?: "ViewModel"
    }

}
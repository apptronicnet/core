package net.apptronic.core.ios.viewmodel

import net.apptronic.core.mvvm.viewmodel.IViewModel
import platform.UIKit.*

class GenericViewBinder : ViewBinder<IViewModel, GenericViewBinder.GenericView>() {

    class GenericView : ViewHolder {

        override val view: UITextView = UITextView().apply {
            this.textColor = UIColor.blackColor
            this.backgroundColor = UIColor.grayColor
            sizeToFit()
            setFont(UIFont.systemFontOfSize(16.0))
        }

    }

    override fun onCreateView(): ViewHolder {
        return GenericView()
    }

    override fun onBindView(view: GenericView, viewModel: IViewModel) {
        view.view.text = viewModel::class.simpleName ?: "ViewModel"
    }

}
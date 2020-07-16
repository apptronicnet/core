package net.apptronic.test.commons_sample_compat_app

import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener

class MainViewBinder : ViewBinder<MainViewModel>() {

    override var layoutResId: Int? = R.layout.activity_main

    override fun onBindView(view: View, viewModel: MainViewModel) {
        with(view) {
            bindClickListener(btnAbout, viewModel.onClickAbout)
            bindClickListener(btnDialog, viewModel.onClickDialog)
        }
    }

}
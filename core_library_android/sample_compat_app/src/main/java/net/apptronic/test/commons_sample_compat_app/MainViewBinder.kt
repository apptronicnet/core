package net.apptronic.test.commons_sample_compat_app

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.test.commons_sample_compat_app.databinding.ActivityMainBinding

class MainViewBinder : ViewBinder<MainViewModel>() {

    override var layoutResId: Int? = R.layout.activity_main

    override fun onBindView() {
        withBinding(ActivityMainBinding::bind) {
            bindClickListener(btnAbout, viewModel.onClickAbout)
            bindClickListener(btnDialog, viewModel.onClickDialog)
        }
    }

}
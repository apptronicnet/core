package net.apptronic.test.commons_sample_compat_app.about

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*
import net.apptronic.core.android.compat.CoreCompatActivity
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.subscribe
import net.apptronic.test.commons_sample_compat_app.R

class AboutActivity : CoreCompatActivity<AboutViewModel>() {

    override fun buildViewModel(parent: Context): AboutViewModel = parent.aboutViewModel()

    override fun onViewModelCreated(savedInstanceState: Bundle?) {
        super.onViewModelCreated(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    override fun onBindViewModel(savedInstanceState: Bundle?) {
        super.onBindViewModel(savedInstanceState)
        viewModel.timerText.subscribe {
            timer.text = it
        }
    }

}
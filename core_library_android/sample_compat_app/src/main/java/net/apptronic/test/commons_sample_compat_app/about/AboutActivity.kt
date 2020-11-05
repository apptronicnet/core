package net.apptronic.test.commons_sample_compat_app.about

import android.os.Bundle
import net.apptronic.core.android.compat.CoreCompatActivity
import net.apptronic.core.context.Context
import net.apptronic.test.commons_sample_compat_app.databinding.ActivityAboutBinding

class AboutActivity : CoreCompatActivity<AboutViewModel>() {

    override fun buildViewModel(parent: Context): AboutViewModel = parent.aboutViewModel()

    lateinit var viewBinding: ActivityAboutBinding

    override fun onViewModelAttached(savedInstanceState: Bundle?) {
        super.onViewModelAttached(savedInstanceState)
        viewBinding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    override fun onBindViewModel(savedInstanceState: Bundle?) {
        super.onBindViewModel(savedInstanceState)
        with(viewBinding) {
            viewModel.timerText.subscribe {
                timer.text = it
            }
        }
    }

}
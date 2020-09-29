package net.apptronic.test.commons_sample_compat_app.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_welcome.*
import net.apptronic.core.android.compat.CoreCompatFragment
import net.apptronic.core.component.context.Context
import net.apptronic.test.commons_sample_compat_app.R

class WelcomeFragment : CoreCompatFragment<WelcomeViewModel>() {

    override fun buildViewModel(parent: Context): WelcomeViewModel = parent.welcomeViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.text.subscribe {
            welcomeMessage.text = it
        }
        btnNext.setOnClickListener {
            viewModel.onClickNext.sendEvent()
        }
    }

}
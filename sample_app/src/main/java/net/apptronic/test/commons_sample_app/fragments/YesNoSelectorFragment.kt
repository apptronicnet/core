package net.apptronic.test.commons_sample_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.models.YesNoSelectorViewModel

class YesNoSelectorFragment : BaseFragment<YesNoSelectorViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.yes_no_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnYes.setOnClickListener { model.onClickBtnYes.sendEvent() }
        btnNo.setOnClickListener { model.onClickBtnNo.sendEvent() }
        btnMaybe.setOnClickListener { model.onClickBtnMaybe.sendEvent() }
    }

}
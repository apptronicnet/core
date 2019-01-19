package net.apptronic.test.commons_sample_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.common.android.ui.components.fragment.BaseFragment
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.models.StartScreenModel

/**
 * Sample fragment creates screen View and binds it to ViewModel
 */
class SampleFragment : BaseFragment<StartScreenModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sample_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
package net.apptronic.test.commons_sample_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.new_input_fragment.*
import net.apptronic.common.android.ui.components.fragment.BaseFragment
import net.apptronic.common.android.ui.components.submodels.bind
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.models.NewInputScreenModel

/**
 * Sample fragment creates screen View and binds it to ViewModel
 */
class NewInputScreenFragment : BaseFragment<NewInputScreenModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_input_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newInputEditText.bind(model.newInput)
        btnSubmit.setOnClickListener { model.submitBtnClicked.sendEvent() }
    }

}
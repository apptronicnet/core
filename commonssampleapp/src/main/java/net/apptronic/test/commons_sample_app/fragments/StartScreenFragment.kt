package net.apptronic.test.commons_sample_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.start_screen_fragment.*
import net.apptronic.common.android.ui.components.fragment.BaseFragment
import net.apptronic.common.android.ui.components.submodels.bind
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.models.StartScreenModel

/**
 * Sample fragment creates screen View and binds it to ViewModel
 */
class StartScreenFragment : BaseFragment<StartScreenModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputValue.bind(model.lastInput)
        inputLength.bind(model.lastInputLength)
        btnRequestNewInput.setOnClickListener {
            model.requestNewInputEvent.sendEvent()
        }
    }

}
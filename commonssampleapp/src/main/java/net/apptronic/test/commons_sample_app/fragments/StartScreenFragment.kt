package net.apptronic.test.commons_sample_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.start_screen_fragment.*
import net.apptronic.common.android.mvvm.components.fragment.BaseFragment
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.models.StartScreenModel

/**
 * Sample fragment creates screen View and binds it to Component
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
        model.someText.subscribe {
            inputValue.text = it
        }
        model.someTextLength.subscribe {
            inputLength.text = it.toString()
        }
        btnRequestNewText.setOnClickListener {
            model.onUserClickButtonRequestNewInput.sendEvent()
        }
        btnSelector.setOnClickListener {
            model.onUserClickButtonSelector.sendEvent()
        }
        model.showToastEvent.subscribe {
            Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
        }
    }

}
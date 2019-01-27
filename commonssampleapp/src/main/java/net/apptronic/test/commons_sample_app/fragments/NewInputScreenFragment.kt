package net.apptronic.test.commons_sample_app.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.new_input_fragment.*
import net.apptronic.common.android.mvvm.components.fragment.BaseFragment
import net.apptronic.common.core.mvvm.generic.bind
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.models.NewInputScreenModel

/**
 * Sample fragment creates screen View and binds it to ViewModel
 */
class NewInputScreenFragment : BaseFragment<NewInputScreenModel>() {

    private var dialog: ProgressDialog? = null

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
        btnSubmit.setOnClickListener {
            model.submitBtnClicked.sendEvent()
        }
        model.isProgressBarVisible.subscribe {
            if (it) {
                dialog = ProgressDialog(context!!).apply {
                    show()
                }
            } else {
                dialog?.dismiss()
                dialog = null
            }
        }
        model.progressBarCountDown.subscribe {
            dialog?.setProgressText("Left $it seconds")
        }
    }

    class ProgressDialog(context: Context) : Dialog(context) {

        init {
            setContentView(R.layout.dialog_layout)
        }

        fun setProgressText(text: String) {
            findViewById<TextView>(R.id.counter).setText(text)
        }

    }

}
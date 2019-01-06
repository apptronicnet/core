package net.apptronic.test.commons_sample_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.sample_fragment.*
import net.apptronic.common.android.ui.components.fragment.BaseFragment
import net.apptronic.common.android.ui.viewmodel.entity.behaviorextensions.savesTextChangesTo
import net.apptronic.common.android.ui.viewmodel.entity.behaviorextensions.showsTextFrom
import net.apptronic.common.android.ui.viewmodel.entity.behaviorextensions.usesTextColorFrom
import net.apptronic.common.android.ui.viewmodel.entity.sendsClicksTo
import net.apptronic.common.android.ui.viewmodel.entity.sendsTextChangeEventsTo

/**
 * Sample fragment creates screen View and binds it to ViewModel
 */
class SampleFragment : BaseFragment<SampleViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setModel(SampleViewModel(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sample_fragment, container, false)
    }

    override fun onBindModel(view: View, model: SampleViewModel) {
        model.title.subscribe { titleTv.text = it }
        btnRefreshTitle.setOnClickListener { model.onClickRefreshTitle.sendEvent() }
        edtSomeInput sendsTextChangeEventsTo model.userInputUpdates
        edtSomeInput savesTextChangesTo model.userInputValue
        btnConfirmInput sendsClicksTo model.onClickConfirmInputEvent
        editedText showsTextFrom model.currentInputText
        confirmedText showsTextFrom model.confirmedInputText.text
        confirmedText usesTextColorFrom model.confirmedInputText.textColor
        secondsCounter.showsTextFrom(model.secondCounter)
        model.toastOnPause.subscribe {
            Toast.makeText(context, "onPause()!!!", Toast.LENGTH_SHORT).show()
        }
    }

}
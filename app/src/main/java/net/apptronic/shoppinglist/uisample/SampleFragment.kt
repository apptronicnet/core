package net.apptronic.shoppinglist.uisample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sample_fragment.*
import net.apptronic.common.android.ui.components.BaseFragment
import net.apptronic.common.android.ui.viewmodel.entity.savesTextChangesTo
import net.apptronic.common.android.ui.viewmodel.entity.sendsClicksTo
import net.apptronic.common.android.ui.viewmodel.entity.sendsTextChangeEventsTo
import net.apptronic.common.android.ui.viewmodel.entity.showsTextFrom
import net.apptronic.shoppinglist.R

/**
 * Sample fragment creates screen View and binds it to ViewModel
 */
class SampleFragment : BaseFragment<SampleViewModel>() {

    override fun onCreateModel(): SampleViewModel {
        return SampleViewModel(this).apply {
            SamplePresenter(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sample_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TextView [id=titleTv] will show text from property [title]
        titleTv showsTextFrom model().title
        // Button [id=btnRefreshTitle] will send click events to UserEvent [onClickRefreshTitle]
        btnRefreshTitle sendsClicksTo model().onClickRefreshTitle
        // EditText [id=edtSomeInput] will send text changes events to UserEvent [userInputUpdates]
        edtSomeInput sendsTextChangeEventsTo model().userInputUpdates
        // EditText [id=edtSomeInput] will save text changes to Property [userInputValue]
        edtSomeInput savesTextChangesTo model().userInputValue
        // Button [id=btnConfirmInput] will send click events to UserEvent [onClickConfirmInputEvent]
        btnConfirmInput sendsClicksTo model().onClickConfirmInputEvent
        // TextView [id=editedText] will show text from property [currentInputText]
        editedText showsTextFrom model().currentInputText
        // TextView [id=confirmedText] will show text from property [confirmedInputText]
        confirmedText showsTextFrom model().confirmedInputText
    }

}
package net.apptronic.shoppinglist.uisample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sample_fragment.*
import net.apptronic.common.android.ui.components.BaseFragment
import net.apptronic.common.android.ui.components.FragmentViewModel
import net.apptronic.common.android.ui.viewmodel.entity.handleClicks
import net.apptronic.common.android.ui.viewmodel.entity.handleInput
import net.apptronic.common.android.ui.viewmodel.entity.textToTextView
import net.apptronic.common.android.ui.viewmodel.setupView
import net.apptronic.shoppinglist.R

class SampleFragment : BaseFragment() {

    var model: SampleViewModel? = null

    override fun onCreateModel(): FragmentViewModel {
        return SampleViewModel(this).apply {
            model = this
            SamplePresenter(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sample_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.setupView {
            title.textToTextView(titleTv)
            onClickRefreshTitle.handleClicks(btnRefreshTitle)
            textInput.handleInput(edtSomeInput)
            userInput.handleInput(edtSomeInput)
            onClickConfirmInput.handleClicks(btnConfirmInput)
            userInputText.textToTextView(editedText)
            userConfirmedInputText.textToTextView(confirmedText)
        }
    }

}
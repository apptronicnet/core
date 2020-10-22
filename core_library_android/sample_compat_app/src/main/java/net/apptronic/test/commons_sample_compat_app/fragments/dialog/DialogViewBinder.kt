package net.apptronic.test.commons_sample_compat_app.fragments.dialog

import androidx.fragment.app.DialogFragment
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.test.commons_sample_compat_app.R
import net.apptronic.test.commons_sample_compat_app.databinding.SampleDialogBinding

class DialogViewBinder(
    private val fragment: DialogFragment
) : ViewBinder<DialogViewModel>() {

    override var layoutResId: Int? = R.layout.sample_dialog

    override fun onBindView() {
        withBinging(SampleDialogBinding::bind) {
            btnClose.setOnClickListener {
                fragment.dismiss()
            }
        }
    }

}
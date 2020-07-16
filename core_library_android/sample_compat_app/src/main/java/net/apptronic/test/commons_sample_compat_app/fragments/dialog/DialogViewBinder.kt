package net.apptronic.test.commons_sample_compat_app.fragments.dialog

import android.view.View
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.sample_dialog.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.test.commons_sample_compat_app.R

class DialogViewBinder(
    private val fragment: DialogFragment
) : ViewBinder<DialogViewModel>() {

    override var layoutResId: Int? = R.layout.sample_dialog

    override fun onBindView(view: View, viewModel: DialogViewModel) {
        with(view) {
            btnClose.setOnClickListener {
                fragment.dismiss()
            }
        }
    }

}
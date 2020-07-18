package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.bottomsheet.BottomSheetViewModel

class BottomSheetBinder : ViewBinder<BottomSheetViewModel>() {

    override var layoutResId: Int? = R.layout.bottom_sheet

    override fun onBindView(view: View, viewModel: BottomSheetViewModel) {
        with(view) {
            bindClickListener(btnHide, viewModel::onClickHide)
        }
    }

}
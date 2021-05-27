package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.bottomsheet.BottomSheetViewModel
import net.apptronic.test.commons_sample_app.databinding.BottomSheetBinding

class BottomSheetViewBinder : ViewBinder<BottomSheetViewModel>() {

    override var layoutResId: Int? = R.layout.bottom_sheet

    override fun onBindView() = withBinding(BottomSheetBinding::bind) {
        bindClickListener(btnReplace, viewModel::onClickReplace)
        bindClickListener(btnHide, viewModel::onClickHide)
    }

}
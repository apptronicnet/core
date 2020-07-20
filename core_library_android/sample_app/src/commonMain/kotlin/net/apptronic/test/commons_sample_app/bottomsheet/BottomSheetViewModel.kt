package net.apptronic.test.commons_sample_app.bottomsheet

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.transition.AppTransition

fun Contextual.bottomSheetViewModel() = BottomSheetViewModel(viewModelContext())

class BottomSheetViewModel(context: ViewModelContext) : ViewModel(context) {

    fun onClickHide() {
        closeSelf(AppTransition.BottomSheet)
    }

}
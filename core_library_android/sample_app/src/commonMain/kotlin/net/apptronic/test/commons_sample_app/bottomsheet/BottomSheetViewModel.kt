package net.apptronic.test.commons_sample_app.bottomsheet

import net.apptronic.core.commons.routing.injectNavigationRouter
import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext
import net.apptronic.test.commons_sample_app.ReplaceBottomSheet
import net.apptronic.test.commons_sample_app.transition.AppTransition

fun Contextual.bottomSheetViewModel() = BottomSheetViewModel(viewModelContext())

class BottomSheetViewModel(context: ViewModelContext) : ViewModel(context) {

    private val router = injectNavigationRouter()

    fun onClickReplace() {
        router.sendCommandsSync(ReplaceBottomSheet())
    }

    fun onClickHide() {
        closeSelf(AppTransition.BottomSheet)
    }

}
package net.apptronic.test.commons_sample_app.bottomsheet

import net.apptronic.core.commons.routing.injectNavigationRouter
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.ReplaceBottomSheet
import net.apptronic.test.commons_sample_app.transition.AppTransition

fun Contextual.bottomSheetViewModel() = BottomSheetViewModel(childContext())

class BottomSheetViewModel(context: Context) : ViewModel(context) {

    private val router = injectNavigationRouter()

    fun onClickReplace() {
        router.sendCommandsSync(ReplaceBottomSheet())
    }

    fun onClickHide() {
        closeSelf(AppTransition.BottomSheet)
    }

}
package net.apptronic.test.commons_sample_app.visibility

import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.commons.switchModel
import net.apptronic.core.viewmodel.viewModelContext

fun Contextual.visibilityDemoViewModel() = VisibilityDemoViewModel(viewModelContext())

class VisibilityDemoViewModel(context: ViewModelContext) : ViewModel(context) {

    val isInterceptAnimations = switchModel(true)

    val isSimplyVisible = switchModel(true)
    val isSimplyFadeIn = switchModel(true)
    val isSimplyFadeOut = switchModel(true)
    val isSimplyFades = switchModel(true)

    val isVisibleAnimatedSwitch1 = switchModel(true)
    val isVisibleAnimatedSwitch2 = switchModel(true)

    val isVisibleTransitionBuilder1 = switchModel(true)
    val isVisibleTransitionBuilder2 = switchModel(true)

}
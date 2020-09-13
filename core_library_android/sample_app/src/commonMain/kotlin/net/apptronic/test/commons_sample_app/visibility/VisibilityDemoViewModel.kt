package net.apptronic.test.commons_sample_app.visibility

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.mvvm.common.switchModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

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
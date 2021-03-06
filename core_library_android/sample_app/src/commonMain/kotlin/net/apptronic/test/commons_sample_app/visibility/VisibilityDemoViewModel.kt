package net.apptronic.test.commons_sample_app.visibility

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.commons.switchModel

fun Contextual.visibilityDemoViewModel() = VisibilityDemoViewModel(childContext())

class VisibilityDemoViewModel(context: Context) : ViewModel(context) {

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
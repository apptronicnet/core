package net.apptronic.test.commons_sample_app.animation

import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext

fun Contextual.animationDemoViewModel() = AnimationDemoViewModel(viewModelContext())

class AnimationDemoViewModel(context: ViewModelContext) : ViewModel(context)
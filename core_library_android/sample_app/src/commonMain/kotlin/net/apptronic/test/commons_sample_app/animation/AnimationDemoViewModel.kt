package net.apptronic.test.commons_sample_app.animation

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun Contextual.animationDemoViewModel() = AnimationDemoViewModel(viewModelContext())

class AnimationDemoViewModel(context: ViewModelContext) : ViewModel(context)
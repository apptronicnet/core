package net.apptronic.test.commons_sample_app.animation

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.viewmodel.ViewModel

fun Contextual.animationDemoViewModel() = AnimationDemoViewModel(childContext())

class AnimationDemoViewModel(context: Context) : ViewModel(context)
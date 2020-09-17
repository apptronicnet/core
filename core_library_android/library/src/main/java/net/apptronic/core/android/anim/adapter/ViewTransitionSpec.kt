package net.apptronic.core.android.anim.adapter

import android.view.View
import net.apptronic.core.android.anim.transition.ViewTransitionDirection

class ViewTransitionSpec(
    val enter: View,
    val exit: View,
    val container: View,
    val duration: Long,
    val transitionSpec: Any?,
    val direction: ViewTransitionDirection? = null
)
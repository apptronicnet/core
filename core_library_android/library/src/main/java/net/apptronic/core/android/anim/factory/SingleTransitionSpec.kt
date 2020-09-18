package net.apptronic.core.android.anim.factory

import android.view.View

class SingleTransitionSpec(
    val target: View,
    val container: View,
    val duration: Long,
    val transitionSpec: Any?
)
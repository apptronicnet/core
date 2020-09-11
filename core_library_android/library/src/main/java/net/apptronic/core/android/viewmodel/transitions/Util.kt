package net.apptronic.core.android.viewmodel.transitions

import android.view.View

fun View.getTransitionParent(container: View? = null): View {
    return container ?: parent as? View ?: this
}
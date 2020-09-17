package net.apptronic.core.android.viewmodel.transitions

import android.view.View

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun View.getTransitionParent(container: View? = null): View {
    return container ?: parent as? View ?: this
}
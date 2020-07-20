package net.apptronic.core.android.viewmodel.transitions

import android.view.View

class ViewSwitch(
    val entering: View?,
    val exiting: View?,
    val container: View,
    val isNewOnFront: Boolean
)
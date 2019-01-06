package net.apptronic.common.android.ui.viewmodel.entity.behaviorextensions

import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.common.android.ui.viewmodel.entity.PropertyNotSetException
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty

fun <A1> ViewModel.whenChanged(
    a1: ViewModelProperty<A1>,
    block: (A1) -> Unit
) {
    forEachChange(a1) {
        try {
            block(a1.get())
        } catch (e: PropertyNotSetException) {
            // ignore
        }
    }
}
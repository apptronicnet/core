package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel

infix fun View.setEnabledDisabledFrom(target: Predicate<Boolean>): EnableDisableBinding {
    return EnableDisableBinding(this, target)
}

class EnableDisableBinding(
    private val view: View,
    private val target: Predicate<Boolean>
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        target.subscribe {
            view.isEnabled = it
        }
    }

}
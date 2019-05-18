package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.ViewModel

infix fun View.setVisibleGoneFrom(target: Entity<Boolean>): VisibleGoneBinding {
    return VisibleGoneBinding(this, target)
}

class VisibleGoneBinding(
    private val view: View,
    private val target: Entity<Boolean>
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        target.subscribe { isVisible ->
            view.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

}
package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.subscribe

infix fun View.setVisibleInvisibleFrom(target: Predicate<Boolean>): VisibleInvisibleBinding {
    return VisibleInvisibleBinding(this, target)
}

class VisibleInvisibleBinding(
    private val view: View,
    private val target: Predicate<Boolean>
) : Binding() {


    override fun onBind() {
        target.subscribe { isVisible ->
            view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        }
    }

}
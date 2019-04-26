package net.apptronic.core.android.viewmodel.bindings

import android.widget.TextView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.ViewToPredicateBinding
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.subscribe

class EnableDisableBinding : ViewToPredicateBinding<TextView, Boolean, Predicate<Boolean>> {

    override fun performBinding(
        binding: ViewModelBinding<*>,
        view: TextView,
        target: Predicate<Boolean>
    ) {
        target.subscribe {
            view.isEnabled = it
        }
    }

}
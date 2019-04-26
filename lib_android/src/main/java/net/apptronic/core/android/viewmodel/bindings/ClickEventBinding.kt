package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.ViewToPredicateBinding
import net.apptronic.core.component.entity.entities.ComponentEvent

class ClickEventBinding : ViewToPredicateBinding<View, Unit, ComponentEvent<Unit>> {

    override fun performBinding(
        binding: ViewModelBinding<*>,
        view: View,
        target: ComponentEvent<Unit>
    ) {
        view.setOnClickListener {
            target.sendEvent(Unit)
        }
    }

}
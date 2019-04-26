package net.apptronic.core.android.viewmodel.bindings

import android.widget.TextView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.ViewToPredicateBinding
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.subscribe

class TextBinding : ViewToPredicateBinding<TextView, String, Predicate<String>> {

    override fun performBinding(
        binding: ViewModelBinding<*>,
        view: TextView,
        target: Predicate<String>
    ) {
        target.subscribe { text ->
            view.text = text
        }
    }

}
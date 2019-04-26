package net.apptronic.core.android.viewmodel.bindings

import android.widget.ImageView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.ViewToPredicateBinding
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.subscribe

class ImageResourceBinding : ViewToPredicateBinding<ImageView, Int, Predicate<Int>> {

    override fun performBinding(
        binding: ViewModelBinding<*>,
        view: ImageView,
        target: Predicate<Int>
    ) {
        target.subscribe {
            view.setImageResource(it)
        }
    }

}
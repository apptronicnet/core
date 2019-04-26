package net.apptronic.core.android.viewmodel.bindings

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.ViewToPredicateBinding
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.subscribe

class ImageTintColorBinding : ViewToPredicateBinding<ImageView, Int, Predicate<Int>> {

    override fun performBinding(
        binding: ViewModelBinding<*>,
        view: ImageView,
        target: Predicate<Int>
    ) {
        target.subscribe { color ->
            ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color))
        }
    }

}
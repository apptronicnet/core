package net.apptronic.core.android.viewmodel.bindings

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel

infix fun ImageView.setImageTintFrom(target: Predicate<Int>): ImageTintColorBinding {
    return ImageTintColorBinding(this, target)
}

class ImageTintColorBinding(
    private val view: ImageView,
    private val target: Predicate<Int>
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        target.subscribe { color ->
            ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color))
        }
    }

}
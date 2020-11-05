package net.apptronic.core.android.viewmodel.bindings

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.entity.Entity
import net.apptronic.core.viewmodel.IViewModel

fun BindingContainer.bindImageTint(imageView: ImageView, target: Entity<Int>) {
    add(ImageTintColorBinding(imageView, target))
}

class ImageTintColorBinding(
    private val view: ImageView,
    private val target: Entity<Int>
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        target.subscribe { color ->
            ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color))
        }
    }

}
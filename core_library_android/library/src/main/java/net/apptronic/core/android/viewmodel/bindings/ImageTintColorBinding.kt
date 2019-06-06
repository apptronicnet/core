package net.apptronic.core.android.viewmodel.bindings

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.ViewModel

infix fun ImageView.setImageTintFrom(target: Entity<Int>): ImageTintColorBinding {
    return ImageTintColorBinding(this, target)
}

class ImageTintColorBinding(
    private val view: ImageView,
    private val target: Entity<Int>
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        target.subscribe { color ->
            ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color))
        }
    }

}
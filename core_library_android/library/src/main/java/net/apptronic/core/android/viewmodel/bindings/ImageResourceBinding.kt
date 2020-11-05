package net.apptronic.core.android.viewmodel.bindings

import android.widget.ImageView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.entity.Entity
import net.apptronic.core.viewmodel.IViewModel

fun BindingContainer.bindImageResource(imageView: ImageView, target: Entity<Int>) {
    add(ImageResourceBinding(imageView, target))
}

class ImageResourceBinding(
    private val view: ImageView,
    private val target: Entity<Int>
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        target.subscribe {
            view.setImageResource(it)
        }
    }

}
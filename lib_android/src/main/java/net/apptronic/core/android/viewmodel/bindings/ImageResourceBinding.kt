package net.apptronic.core.android.viewmodel.bindings

import android.widget.ImageView
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel

infix fun ImageView.setImageResourceFrom(target: Predicate<Int>): ImageResourceBinding {
    return ImageResourceBinding(this, target)
}

class ImageResourceBinding(
    private val view: ImageView,
    private val target: Predicate<Int>
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        target.subscribe {
            view.setImageResource(it)
        }
    }

}
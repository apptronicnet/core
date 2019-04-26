package net.apptronic.test.commons_sample_app.list

import android.view.View
import kotlinx.android.synthetic.main.list_item_image.view.*
import net.apptronic.core.android.component.functions.resourceToColor
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.bindings.ClickActionBinding
import net.apptronic.core.android.viewmodel.bindings.ImageResourceBinding
import net.apptronic.core.android.viewmodel.bindings.ImageTintColorBinding
import net.apptronic.core.android.viewmodel.bindings.TextBinding
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.test.commons_sample_app.R

class ListItemImageView : AndroidView<ListItemImageViewModel>() {

    init {
        layoutResId = R.layout.list_item_image
    }

    override fun onCreateBinding(
        view: View,
        viewModel: ListItemImageViewModel
    ): ViewModelBinding<ListItemImageViewModel> {
        return createBinding(view, viewModel) {
            with(view) {
                listItemImgIndex.bindTo(TextBinding(), viewModel.index.map { it.toString() })
                listItemImgContent.bindTo(ImageResourceBinding(), viewModel.imageRes)
                listItemImgContent.bindTo(
                    ImageTintColorBinding(),
                    viewModel.colorRes.resourceToColor(context)
                )
                view.bindTo(ClickActionBinding(), viewModel::onBodyClick)
                listItemImgRemove.bindTo(ClickActionBinding(), viewModel::onRemoveButtonClick)
            }
        }
    }

}